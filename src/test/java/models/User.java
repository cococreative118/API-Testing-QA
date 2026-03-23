package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// 关键注解：告诉 Jackson "JSON 里有的字段，我的 POJO 没有定义，请直接忽略，不要报错"
// 这在真实项目里必须加！后端接口经常返回几十个字段，你只需要关心其中几个。
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    // 字段名必须和 JSON 里的 key 一模一样！Jackson 靠字段名做映射
    public String name;
    public String email;
    public String username;

    // 无参构造器（Jackson 反序列化时必须有！）
    public User() {}

    // 有参构造器（方便在 POST 请求里直接 new User(...) 创建对象）
    public User(String name, String email, String username) {
        this.name = name;
        this.email = email;
        this.username = username;
    }
}
