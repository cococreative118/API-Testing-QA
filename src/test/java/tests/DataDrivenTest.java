package tests;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class DataDrivenTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://dummyjson.com";
    }

    // @DataProvider：专门提供数据的"水管"，不执行任何业务逻辑
    // 返回 Object[][] 二维数组：每一行 = 一组测试数据
    @DataProvider(name = "loginScenarios")
    public Object[][] loginScenarios() {
        return new Object[][] {
            // { username,          password,       预期状态码, 场景描述       }
            { "emilys",            "emilyspass",   200,        "正确账号密码" },
            { "emilys",            "wrongpassword", 400,       "账号正确密码错" },
            { "nonexistentuser",   "anypassword",   400,       "账号不存在" },
        };
    }

    // dataProvider = "loginScenarios" 把上面的水管接到这个方法里
    // TestNG 会自动把每一行数据作为参数注入进来，跑三次
    @Test(dataProvider = "loginScenarios")
    public void testLoginWithMultipleScenarios(String username, String password,
                                               int expectedStatus, String scenario) {
        System.out.println("🧪 测试场景: " + scenario);

        Map<String, Object> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);

        given()
            .header("Content-Type", "application/json")
            .body(body)
        .when()
            .post("/auth/login")
        .then()
            .statusCode(expectedStatus); // ⚡ 直接用参数里的预期状态码做断言！

        System.out.println("✅ [" + scenario + "] 验证通过！预期状态码 " + expectedStatus);
    }
}
