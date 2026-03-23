package tests;

import io.restassured.RestAssured;
import models.User;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PojoTest extends BaseApiTest   {

    // @BeforeClass
    // public void setup() {
    //     RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    // }

    @Test
    public void testDeserializeResponse() {
        // 【反序列化】：把后端返回的 JSON 字符串，自动"装配"到我们的 User 对象里！
        // .as(User.class) 告诉 Jackson：把这段 JSON 转成一个 User 对象
        User user = given(requestSpec)
            .when()
                .get("/users/1")
            .then()
                .statusCode(200)
                .extract().as(User.class); // ⚡ 这里是魔法！

        // 现在直接用对象的字段，不需要写 jsonPath().getString("xxx")
        System.out.println("⚡️ 用户名: " + user.name);
        System.out.println("⚡️ 邮箱: " + user.email);

        // 断言超级直观！
        Assert.assertEquals(user.name, "Leanne Graham");
        Assert.assertEquals(user.email, "Sincere@april.biz");
    }
}
