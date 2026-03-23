package tests;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest extends BaseApiTest {
    @Test
    public void testCreateNewUser() {
        //RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        // 1. 【准备小纸条】使用 Map 来构造 JSON 数据
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Coco QA");
        requestBody.put("job", "Senior Automation Engineer");

        // 2. 发射 POST 请求
        Response response =
                given(requestSpec)
                    .header("Content-Type", "application/json") // 必写！是告诉服务器**"我发给你的 Body 是什么格式的"
                    .body(requestBody) // 把小纸条塞进请求里
                .when()
                    .post("/users") // 动作变成了 POST，而且往往路径更短（代表在全体 user 资源池里新建一个）
                .then()
                    .log().all() // 记得打印出来看看
                    .statusCode(201) // 面试点：POST 创建成功，标准状态码应该是 201 Created，而不是 200！
                    .extract().response();
        // 3. 【见证奇迹的时刻】
        // 对于新建的数据，后端通常会自动生成一个 ID 并返回给你。
        // 请你把这个新建的 ID 提取出来打印在控制台上！
        int newUserId = response.jsonPath().getInt("id");
        System.out.println("⚡️后端为 Coco 分配的新 ID 是: " + newUserId);
    }
}

