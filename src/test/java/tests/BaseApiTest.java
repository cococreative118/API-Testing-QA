package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {

    protected RequestSpecification requestSpec; // 通用请求规格，子类都能用

    @BeforeClass// 每个继承此类的测试类运行前都会初始化一次（比 @BeforeSuite 更安全）


    public void globalSetup() {
        // 从系统属性读取环境，默认 jsonplaceholder
        String env = System.getProperty("env", "jsonplaceholder");
        String baseUrl;

        switch (env) {
            case "dummyjson": baseUrl = "https://dummyjson.com"; break;
            case "qa":        baseUrl = "https://api.qa.mycompany.com"; break;
            default:          baseUrl = "https://jsonplaceholder.typicode.com";
        } //env的值等run时候体现：
        // 情况一：什么都不传mvn test -Dtest=GetUsersTest
        // 情况二：指定环境mvn test -Denv=qa -Dtest=GetUsersTest
        // 情况三：指定环境mvn test -Denv=dummyjson -Dtest=GetUsersTest
        
        RestAssured.baseURI = baseUrl;

        // RequestSpecBuilder：把通用的 Header 等配置统一打包
        requestSpec = new RequestSpecBuilder()
            .addHeader("Content-Type", "application/json") // 把通用 Header 预埋进去
            .addFilter(new AllureRestAssured())             // ⚡ Allure 过滤器：自动把每次请求/响应截图进报告！
            .build(); // 打包完成，生成 RequestSpecification 对象

        System.out.println("🌐 测试环境: " + env + " → " + baseUrl);
    }
}
