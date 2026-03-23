package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserLifecycleTest {

    // 用一个类变量保存跨方法共享的 ID
    private int createdUserId;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    // Step 1: 创建用户，把返回的 ID 存起来
    @Test(priority = 1)
    public void testCreateUser() {
        Map<String, Object> body = new HashMap<>();
        body.put("name", "Coco QA");
        body.put("job", "Automation Engineer");

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .body(body)
                        .when()
                        .post("/users")
                        .then()
                        .statusCode(201)
                        .extract().response();

        createdUserId = response.jsonPath().getInt("id");
        System.out.println("✅ Step 1 - 用户创建成功，ID 是: " + createdUserId);
        Assert.assertTrue(createdUserId > 0, "ID 应该是正数！");
    }

    // Step 2: 演示"用变量动态拼接 URL"的代码模式
    // ⚠️ 注意：JSONPlaceholder 是 Mock API，POST 不会真正把数据存入数据库（id 11 实际不存在）
    // 在真实的 QA/Staging 环境中，这里直接用 createdUserId 就能查到！
    @Test(priority = 2)
    public void testGetCreatedUser() {
        int lookupId = 1; // 用真实存在的用户 (Mock API 里有 1-10)
        System.out.println("🔍 Step 2 - 演示动态拼接 URL：/users/" + lookupId);
        System.out.println("   (在真实后端里，这里应该用 createdUserId=" + createdUserId + ")");

        given()
        .when()
            .get("/users/" + lookupId) // ⚡ 关键：把变量动态拼进 URL！
        .then()
            .statusCode(200);

        System.out.println("✅ Step 2 - 查询通过！");
    }

    // Step 3: 删除这个用户（清理测试数据）
    @Test(priority = 3)
    public void testDeleteUser() {
        System.out.println("🗑️ Step 3 - 删除 ID 为 " + createdUserId + " 的用户...");

        given()
                .when()
                .delete("/users/" + createdUserId) // DELETE 动作
                .then()
                .statusCode(200);

        System.out.println("✅ Step 3 - 删除成功，测试数据已清理！");
    }
}
