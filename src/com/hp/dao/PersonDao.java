package com.hp.dao;

import com.hp.bean.Person;
import com.hp.dto.PersonDTO;

import java.util.List;
import java.util.Map;

public interface PersonDao {
    // 全查
    List<Person> selectAll(); //方法名和xml中的id名 必须一致
    //根据性别查询
    List<Person> selectPersonBySex(int sex);  //(int sex,String name); mybatis不支持两个参数，支持一个
    //查询 总条数
    long selectCount();
    // 查询总条数 + 多个 参数 第一种方式  2个参数都是person类中的属性，所以直接可以把 person作为参数
    long selectCountByParam01(Person person);
    //查询 性别 和生日 当查询出的sql 语句不确定的一条的时候， 返回值一定是要用 list
    //当多表联查的时候， 请求的参数一定要为 map 或者是 自已写个实体类 应用场景： 多表联查
    List<Person> selectCountByParam02(Map map);

    //1. 查询 分值最高的人是谁 ？
    List<Person> selectMaxname();

    //2. 所有女生的平均分值是多少 ？
//    Double selectAVG(Person person);

    //男生女生的平均值各是多少？ select gender,avg(score) from person group by gender;
    List<PersonDTO> selectAvgScore();
    //男生和女生的平均分值  大于200的都有什么  用的PersonDTO的方式
    List<PersonDTO> selectAvgScoreParam(int score);

    //男生和女生的平均分值  大于200的都有什么 有参数  用的 Map 的方式做返回值
    List<Map> selectAvgScoreParam02(int score);
    //3.所有人的分数大于100的或者性别是1的
    List<Person> selectCountByParam03(Map map);

    //查询 姓孙的 第一种方式 模糊查询  不要用 拼接的方式去写  $
    List<Person> selectPersonByLike(String name);

    //查询 姓孙的 第二种方式 模糊查询
    List<Person> selectPersonByLike02(String name);

    //查询 姓孙的 第三种方式 模糊查询
    List<Person> selectPersonByLike03(String name);

    //增加一条数据  insert into person (id, name , gender , birthday , address ,score) values(null,#{name}...)
    int insertPerson(Person person);

    // 删除的方法
    int deletePersonById(int id);  //注意： 之后 讲解 动态sql,
    //那么 我们的dao层接口中， 只有基础类型 int,String 不好的，不方便 执行动态sql,对以后的扩展不便
    // 以后 自已写代码 参数 一定是 一个实体类 或者是一个map 获取是 DTO

    //动态sql
    List<Person> dongTaiSelect(Person person);  //动态的sql ，如果参数不是实体类，不是集合， 是个空参数的话，那么没有任何意义
    // 长成 返回值是 List<实体类> 参数 也是 同样的实体类 ，那么 这就是一个典型的 动态sql 语句。。。

    //动态的修改
    int dongtaiUpdate(Person person);

    // 批量删除
    void piliangDel(Map map);  //也可以传实体类 ，或者 List


}
