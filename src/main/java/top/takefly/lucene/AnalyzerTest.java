package top.takefly.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @program: Demo1
 * @description: 分析器使用
 * @author: 戴灵飞
 * @create: 2019-07-20 16:38
 **/
public class AnalyzerTest {

    @Test
    public void tokenStream() throws Exception {
        //创建分析器
        Analyzer analyzer = new StandardAnalyzer();
        //创建tokenStream
//        TokenStream tokenStream = analyzer.tokenStream("test", "The Spring Framework provides a comprehensive programming and configuration model.");
        TokenStream tokenStream = analyzer.tokenStream("test", "我爱中国");
        //添加一个引用，获取每个关键字
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        //添加一个用用，记录开始位置和结束位置
        OffsetAttribute offset = tokenStream.addAttribute(OffsetAttribute.class);
        //将指针重置
        tokenStream.reset();
        //遍历分析结果
        while (tokenStream.incrementToken()) {
            //关键字的起始位置
            //System.out.println("\n\nstart->"+offset.startOffset());
            //取关键字
            System.out.println(charTermAttribute);
            //关键的结束位置
            //System.out.println("end->"+offset.endOffset());
        }
        tokenStream.close();
    }

    @Test
    public void IKAnalyzerTokenStream() throws Exception {
        //创建分析器
        Analyzer analyzer = new IKAnalyzer();
        //创建tokenStream
        TokenStream tokenStream = analyzer.tokenStream("test", "我爱中国");
        //添加一个引用，获取每个关键字
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        //添加一个用用，记录开始位置和结束位置
        OffsetAttribute offset = tokenStream.addAttribute(OffsetAttribute.class);
        //将指针重置
        tokenStream.reset();
        //遍历分析结果
        while (tokenStream.incrementToken()) {
            System.out.println(charTermAttribute);
        }
        tokenStream.close();
    }


    @Test
    public void IKAnalyzerParserTest() throws Exception{
        Analyzer analyzer = new IKAnalyzer();
        //通过IkAnalyzer来获取TokenStream
        TokenStream tokenStream = analyzer.tokenStream("IKAnalyzerParser", "lucene是java编写的最好的工具");
        //获取字符获取的引用
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        //获取指针引用
        OffsetAttribute offset = tokenStream.addAttribute(OffsetAttribute.class);
        //重置指针的位置
        tokenStream.reset();
        //移动指针获取
        while(tokenStream.incrementToken()){
//            System.out.println("\nstart->"+offset.startOffset());
            System.out.println(charTermAttribute);
//            System.out.println("end->"+offset.endOffset());
        }
        tokenStream.close();
    }

    @Test
    public void arraysCopyOf(){
        Object[] elementData = {1 ,2 ,3 ,4,5};
        System.out.println("element's size="+elementData.length);
        elementData = Arrays.copyOf(elementData, 6);
        elementData[5] = 6;
        for(Object obj:elementData){
            System.out.println(obj);
        }

    }

    @Test
    public void arraycopy(){
        Object[] elementData = {1 ,2 ,3 ,4,5 , 6 ,7 , 8 , 9 , 10 , 11, 12};
        elementData = grow(elementData , 1);
        elementData[12] = 13;
        for(Object obj:elementData){
            System.out.println(obj);
        }

    }

    private Object[] grow(Object[] elementData , int capacity){
        Object[] newArr = new Object[elementData.length+capacity];
        System.arraycopy(elementData , 0 , newArr ,  0 , elementData.length);
        return newArr;
    }



}


class Person{
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

class Student{
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
