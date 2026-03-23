package tests;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GetUsersTest extends BaseApiTest {

    @Test
    public void testGetUser() {
        // First initial test
        

        given(requestSpec)// 【前提】：如果你要传 Token、Header，或者带上 JSON Body 小纸条，全写在 given() 屁股后面
        .when()// 【动作】：告诉你要发送什么类型的请求（GET / POST），以及你要访问的具体路径
            .get("/users/1")
        .then()// 【验证】：收网！拿到后面返回结果后，开始对答案！
            .statusCode(200)// 断言状态码必须是 200
            .body("name", equalTo("Leanne Graham")); // 断言返回的 JSON 里的 name 必须等于这个名字
    }

    @Test
    public void testExtractAndPrintUserInfo() {
        //RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        // 我们不再直接 .then() 闷头断言，而是把后端返回的整张"大票据"（Response）存到一个本地变量里慢慢研究！
        io.restassured.response.Response response = 
            given(requestSpec)
            .when()
                .get("/users/1")
            .then()
                .log().all() // 绝招 1：把请求的详细过程和返回的 JSON 全文，漂漂亮亮地打印在终端里给你看！
                .statusCode(200)
                .extract().response(); // 把验证后的最终结果提取出来，交还给左边的 response 变量

        // 绝招 2：像剥洋葱一样精准提取深层数据 (JsonPath)
        // 从 response 这个大票据里，精准抠出 email 字段，并存到我们的 String 变量里！
        String userEmail = response.jsonPath().getString("email");
        
        // 提取嵌套结构（比如我想拿里面 address 的 city 字段，就用点 '.' 连接）
        String userCity = response.jsonPath().getString("address.city");

        // 绝招 3：随时使用拿到的数据（比如打印验证，或者传给下一个接口作为参数）
        System.out.println("-------------------------------------------------");
        System.out.println("⚡️从后端抓取到的用户邮箱是: " + userEmail);
        System.out.println("⚡️从后端抓取到的城市是: " + userCity);
        System.out.println("-------------------------------------------------");

        // 我们还可以对抠出来的数据，用我们最熟悉的 TestNG Assert 进行传统的断言
        org.testng.Assert.assertEquals(userEmail, "Sincere@april.biz");
    }
}
