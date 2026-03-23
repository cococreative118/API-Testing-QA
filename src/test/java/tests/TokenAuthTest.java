package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TokenAuthTest {

    private String authToken; // 类变量，存登录后拿到的 Token

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://dummyjson.com"; // 写在全局公告板上。静态变量（static field）
    }// 之后任意地方的 given() 发请求，都会自动去公告板上读这个 URL

    // Step 1：登录，拿到 Token
    @Test(priority = 1)
    public void testLogin() {
        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("username", "emilys");     // DummyJSON 提供的测试账号
        loginBody.put("password", "emilyspass");

        Response response =
            given()
                .header("Content-Type", "application/json")
                .body(loginBody)
            .when()
                .post("/auth/login")
            .then()
                .log().body()   // 只打印 Body（不打印整个 Header，保持输出简洁）
                .statusCode(200)
                .extract().response();

        // 从响应里提取 accessToken 存起来
        authToken = response.jsonPath().getString("accessToken");
        System.out.println("✅ 登录成功！");
        System.out.println("🎫 Token（前50个字符）: " + authToken.substring(0, 50) + "...");
        Assert.assertNotNull(authToken, "Token 不应该是 null！");
    }

    // Step 2：用 Token 请求需要认证的接口
    @Test(priority = 2)
    public void testGetCurrentUser() {
        System.out.println("🔒 使用 Token 请求受保护的接口 /auth/me ...");

        Response response =
            given()
                .header("Authorization", "Bearer " + authToken) // ⚡ 把 Token 注入 Header！
            .when()
                .get("/auth/me")   // 这个接口：不带 Token 就 401，带了才能看到自己的信息
            .then()
                .statusCode(200)
                .extract().response();

        String username = response.jsonPath().getString("username");
        System.out.println("✅ Token 验证通过！当前登录的用户是: " + username);
        Assert.assertEquals(username, "emilys");
    }
}
