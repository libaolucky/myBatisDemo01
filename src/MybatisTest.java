import com.hp.bean.Person;
import com.hp.dto.PersonDTO;
import com.li.bean.Human;
import com.li.bean.HumanExample;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MybatisTest {
    private SqlSession sqlSession;  //面试题： 讲一下 mybatis的执行流程
    private SqlSessionFactory sqlSessionFactory;
    @Before  //在进行@Test 注解之前，执行的方法， 提取重复的代码的
    public void before() throws Exception {
        //加载并读取xml
        String path="SqlMapConfig.xml";
        InputStream is = Resources.getResourceAsStream(path);
        // sql 连接的 工厂类
        sqlSessionFactory=new SqlSessionFactoryBuilder().build(is);
        sqlSession = sqlSessionFactory.openSession();
        System.out.println("sqlSession = " + sqlSession);
        //sqlSession = org.apache.ibatis.session.defaults.DefaultSqlSession@66480dd7

    }

    //全查  select * from person ----》 讲的点 是  select resultMap
    @Test
    public void test01(){
        List<Person> personList= sqlSession.selectList("com.hp.dao.PersonDao.selectAll");
        for (Person person : personList) {
            System.out.println("person = " + person);
        }
        sqlSession.close();
    }
    // 查询 有条件的语句
    @Test
    public void test02(){
        List<Person> personList=sqlSession.selectList("com.hp.dao.PersonDao.selectPersonBySex",2);
        for (Person person : personList) {
            System.out.println("person = " + person);
        }
        sqlSession.close();
    }
    //查总条数  这个主要学的是  返回的数据类型 和上面的数据类型不一样
    @Test
    public void test03(){
        long  zz= sqlSession.selectOne("com.hp.dao.PersonDao.selectCount");
        System.out.println("zz = " + zz);
        sqlSession.close();
    }
    //带参数的查询   第一种方式
    @Test
    public void test04(){
        Person person=new Person();
        person.setScore(200);
        person.setGender(2);
        
       long o=sqlSession.selectOne("com.hp.dao.PersonDao.selectCountByParam01",person);
        System.out.println("o = " + o);
        sqlSession.close();
    }
    //带参数的查询 第2种方式 ：map 传参--- 多见于  多表联查
    @Test
    public void test05() throws ParseException {
        String date="2020-10-14";
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        Date birthday=sf.parse(date);

        Map map=new HashMap();
        map.put("gender",2); //key 一定要和 #{gender} 值保持一致
        map.put("birthday",birthday); //key 一定要和 #{birthday}值保持一致

        List<Person> list=sqlSession.selectList("com.hp.dao.PersonDao.selectCountByParam02",map);
        for (Person person : list) {
            System.out.println("person = " + person);
        }
        sqlSession.close();
    }

    @Test
    public void test06(){
        List<Person>  zz= sqlSession.selectList("com.hp.dao.PersonDao.selectMaxname");
        for (Person person : zz) {
            System.out.println("person = " + person);
        }
        sqlSession.close();
    }

    //2.所有女生的平均分值是多少 ？
    @Test
    public void test07() throws ParseException {
        Person person=new Person();
        person.setGender(2);
        Double o=sqlSession.selectOne("com.hp.dao.PersonDao.selectAVG",person);
        System.out.println("o = " + o);
        sqlSession.close();
    }
    //分组查询
    @Test
    public void test08(){
       List<PersonDTO> personDTOS= sqlSession.selectList("com.hp.dao.PersonDao.selectAvgScore");
        for (PersonDTO personDTO : personDTOS) {
            System.out.println("personDTO = " + personDTO);
        }
        sqlSession.close();
    }
    //男生和女生的平均分值  大于200的都有什么 分组查询+ 参数 用DTO 返回值的方式
    @Test
    public void test09(){
        List<PersonDTO> personDTOS= sqlSession.selectList("com.hp.dao.PersonDao.selectAvgScoreParam",200);
        for (PersonDTO personDTO : personDTOS) {
            System.out.println("personDTO = " + personDTO);
        }
        sqlSession.close();
    }
    //男生和女生的平均分值  大于200的都有什么 分组查询+ 参数  用map 返回值的方式
    @Test
    public void test010(){
        List<Map> personmap= sqlSession.selectList("com.hp.dao.PersonDao.selectAvgScoreParam02",200);
        for (Map map : personmap) {
            System.out.println("map = " + map);
        }
        sqlSession.close();
    }

    @Test
    public void test11() throws ParseException {
        Map map=new HashMap();
        map.put("score",100);
        map.put("gender",2); //key 一定要和 #{gender} 值保持一致

        List<Person> list=sqlSession.selectList("com.hp.dao.PersonDao.selectCountByParam03",map);
        for (Person person : list) {
            System.out.println("person = " + person);
        }
        sqlSession.close();
    }

    // 查询姓孙的  模糊查询 方式一： 不要用 拼接的方式去写  $
    @Test
    public void test12(){
        Map map=new HashMap();
        map.put("name","孙");
       List<Person> personList=sqlSession.selectList("com.hp.dao.PersonDao.selectPersonByLike",map);
        //There is no getter for property named 'name' in 因为 $ 是拼接的，没有getter这个概念
        for (Person person : personList) {
            System.out.println("person = " + person);
        }
        sqlSession.close();
    }
    // 查询名字带孙的  第二种方式：concat
    @Test
    public void test13(){
        List<Person> personList= sqlSession.selectList("com.hp.dao.PersonDao.selectPersonByLike02","孙");
        for (Person person : personList) {
            System.out.println("person = " + person);
        }
        sqlSession.close();
    }
    // 查询名字带孙的  第三种方式：
    @Test
    public void test14(){
        List<Person> personList= sqlSession.selectList("com.hp.dao.PersonDao.selectPersonByLike03","孙");
        for (Person person : personList) {
            System.out.println("person = " + person);
        }
        sqlSession.close();
    }

    // 以上就是单表的  所有查询！！！

    // 增加  insert into .....
    @Test
    public void test15(){
        Person person=new Person();
        person.setName("啦啦啦");
        person.setGender(1);
        person.setBirthday(new Date());
        person.setAddress("熊昂碧");
        person.setScore(999);

        int  insert=sqlSession.insert("com.hp.dao.PersonDao.insertPerson",person);
        System.out.println("insert = " + insert);
        sqlSession.commit();
        sqlSession.close();
 }

    //删除  delete
    @Test
    public void test16(){
        Integer i=sqlSession.selectOne("com.hp.dao.PersonDao.deletePersonById",19);
        System.out.println("i = " + i);
        sqlSession.commit();
        sqlSession.close();
    }

    // 动态sql, 重点 ， 难点，  高薪的起点
    // 动态sql 其实就是 让达到1条 xml中的语句可以实现  N多种查询
    // 那么  要实现多种查询， 就有一个硬性的条件, 你的参数要多 》》》
    // 1.放弃单个的属性（int,String） 改用实体类  2.参数改用map
    // 今天学的 推翻 昨天学的，那么就需要 总结所学的

    // 第一类： 特性（1） 返回值 ----》 正常表的结果集 ，对应的是 person 实体类
    // (2) 都是 select * 开头的
    //1.1 select * from person   if 如果是  where 后面设参数 那么就是全查的
    //1.2 select * from person where gender=2    where 后面参数是 gender  那么就是单查 gender
    //1.3 select * from person where gender=#{gender} and birthday < #{birthday}
    //1.4 select * from person where name like "%"#{name}"%"
    // 1-4 可以合4个为一个  只需要把 where 后面的参数做个 if 判断

    // 第二类： 特征：（1）返回值----》 一个数，单行单列 非 person实体类，是一个数据类型
    //(2) 都是以 select count(*) 开头的
    //2.1 select count(*) from person;
    //2.2 select count(*) from person where gender=2 and score>100

    // 综合所述！ 以上 sql 可以 进行动态判断形成一个sql !!! 这就叫做 动态sql

    // 以上写的就废了

    // 动态查询
    @Test
    public void test17(){
        Person person=new Person();
        //null 就是全查
        //person.setId(14); //select * from person p WHERE p.id=?
        person.setScore(200);
        person.setGender(2);  // select * from person p WHERE p.gender=? and p.score > ?

        List<Person> personList=sqlSession.selectList("com.hp.dao.PersonDao.dongTaiSelect",person);
        for (Person person1 : personList) {
            System.out.println("person1 = " + person1);
        }
        sqlSession.close();
    }
    //动态修改 其实就是有选择性的修改多个字段，比如说 可以修改女孩的分数，
    @Test
    public void test18(){
        Person person=new Person();
        person.setId(14);
        person.setAddress("英国");
        person.setBirthday(new Date());


        int update=sqlSession.update("com.hp.dao.PersonDao.dongtaiUpdate",person);
        System.out.println("update = " + update);
        sqlSession.commit();
        sqlSession.close();
    }

    //批量删除 delete in(1,2,3,4)
    //构造一个  ids
    @Test
    public void test19(){
       List<Integer> idList=new ArrayList<>();
        idList.add(1);
        idList.add(2);
        idList.add(3);

        Map map=new HashMap();
        map.put("ids",idList);
       int d=sqlSession.delete("com.hp.dao.PersonDao.piliangDel",map);
        System.out.println("d = " + d);
        sqlSession.commit();
        sqlSession.close();
    }

    //以上 代码 不用手写，因为谁写 谁垃圾
    // 以上的 xml的代码 不需要我写！！！ DTO 不需要我写！！！
    //但是要能看懂 了解


    // 这是重点 逆向生成， 公司都用
    // 没有 写一行代码  但是 已经 功能都实现了
    @Test
    public void test20(){
      //  Preparing: select count(*) from human
        //select count(*) from human WHERE gender = 2 and address='西京';
        HumanExample example=new HumanExample(); //创建一个 例子类
        HumanExample.Criteria criteria = example.createCriteria(); //用例子类实现查询的规则获取标准
        //criteria.andGenderEqualTo(2);  //select count(*) from human WHERE ( gender = ? )
        //criteria.andAddressEqualTo("西京");
        //案例： 查询 地址 是西京的人有几个人？  select count(*) from human WHERE address like "%西京%"
        //criteria.andAddressLike("%"+"西京"+"%");
        // 查询 家住在北京 或者 分数是555的人有几个 select count(*) fromr human where address="北京" or scorer=555
        // 因为 criteria 查询标准里没有 or 但是有 in
       // example.or().andAddressEqualTo("北京");
        //example.or().andScoreEqualTo(555.0);  // or 不需要 criteria 类

        // select count(*) fromr human where id=3 or id=4 or id=5
        // 等于 select * fromr human where in(1,4,5)
        List<Integer> ids=new ArrayList<>();
        ids.add(1);
        ids.add(4);
        ids.add(5);
        //example.or().andIdIn(ids);
        criteria.andIdIn(ids);

        //当 example 的 criteria 不用 赋值的时候，则是
        long o=sqlSession.selectOne("com.li.dao.HumanDAO.countByExample",example);
        System.out.println("o = " + o);
        sqlSession.close();
    }


    // 单表的所有
// 查询：
// select * from human;  -----全查   test21();
// select * from human where gendar=2;

    //作业1： 把下面的 测试了！！！
// select * from human where gendar=1;
// select * from human where id=1;
// select * from human where score >80;
// select * from human where score >80 and gendar=1;
// select * from human where score >80 and gendar=1 and address like "%西京%";



    @Test
    public void test21(){
        HumanExample example=new HumanExample();
        List<Human> Humans = sqlSession.selectList("com.li.dao.HumanDAO.selectByExample", example);
        for (Human human : Humans) {
            System.out.println("human = " + human);
        }
        sqlSession.close();
    }

    @Test
    public void test21_01(){
        // select * from human where gendar=2;
        HumanExample example=new HumanExample();
        HumanExample.Criteria criteria = example.createCriteria();
        criteria.andGenderEqualTo(2);

        List<Human> Humans = sqlSession.selectList("com.li.dao.HumanDAO.selectByExample", example);
        for (Human human : Humans) {
            System.out.println("human = " + human);
        }
        sqlSession.close();

    }

    @Test
    public void test21_02(){
        // select * from human where gendar=1;
        HumanExample example=new HumanExample();
        HumanExample.Criteria criteria = example.createCriteria();
        criteria.andGenderEqualTo(1);

        List<Human> Humans = sqlSession.selectList("com.li.dao.HumanDAO.selectByExample", example);
        for (Human human : Humans) {
            System.out.println("human = " + human);
        }
        sqlSession.close();
    }
    @Test
    public void test21_03(){
        // select * from human where id=1;
        HumanExample example=new HumanExample();
        HumanExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(1);

        List<Human> Humans = sqlSession.selectList("com.li.dao.HumanDAO.selectByExample", example);
        for (Human human : Humans) {
            System.out.println("human = " + human);
        }
        sqlSession.close();

    }
    @Test
    public void test21_04(){
        // select * from human where score >80;
        HumanExample example=new HumanExample();
        HumanExample.Criteria criteria = example.createCriteria();
        criteria.andScoreGreaterThan(80.0);
        List<Human> Humans = sqlSession.selectList("com.li.dao.HumanDAO.selectByExample", example);
        for (Human human : Humans) {
            System.out.println("human = " + human);
        }
        sqlSession.close();

    }

    @Test
    public void test21_05(){
        // select * from human where score >80 and gendar=1;
        HumanExample example=new HumanExample();
        HumanExample.Criteria criteria = example.createCriteria();
        criteria.andScoreGreaterThan(80.0);
        criteria.andGenderEqualTo(1);
        List<Human> Humans = sqlSession.selectList("com.li.dao.HumanDAO.selectByExample", example);
        for (Human human : Humans) {
            System.out.println("human = " + human);
        }
        sqlSession.close();

    }
    @Test
    public void test21_06(){
        //  select * from human where score >80 and gendar=1 and address like "%西京%";
        HumanExample example=new HumanExample();
        HumanExample.Criteria criteria = example.createCriteria();
        criteria.andScoreGreaterThan(80.0);
        criteria.andGenderEqualTo(1);
        criteria.andAddressLike("%"+"西京"+"%");
        List<Human> Humans = sqlSession.selectList("com.li.dao.HumanDAO.selectByExample", example);
        for (Human human : Humans) {
            System.out.println("human = " + human);
        }
        sqlSession.close();

    }



}
