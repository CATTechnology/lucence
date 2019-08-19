package top.takefly.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @program: Demo1
 * @description: 用于分析arrayList源码
 * @author: 戴灵飞
 * @create: 2019-07-22 20:28
 **/
public class ArrayListSourceParse {

    private Directory directory;
    private IndexReader indexReader;
    private IndexSearcher indexSearcher;

    //@Before
    public void init() throws Exception{
        directory = FSDirectory.open(new File("D:/javaEE/luke/index").toPath());
        indexReader = DirectoryReader.open(directory);
        indexSearcher = new IndexSearcher(indexReader);
    }

    @Test
    public void query() throws Exception{
        Query query = new TermQuery(new Term("fileName" , "lucene"));
        TopDocs topDocs = indexSearcher.search(query, 10);
        for(ScoreDoc scoreDoc:topDocs.scoreDocs){
            Document document = indexSearcher.doc(scoreDoc.doc);
            String fileName = document.get("fileName");
            String size = document.get("size");
            String content = document.get("content");
            String path = document.get("path");
            System.out.println(fileName);
            System.out.println(size);
            System.out.println(content);
            System.out.println(path);
            System.out.println("-----------------------------\n");
        }
        indexReader.close();
    }

    @Test
    public void checkIndex(){
        Objects.checkIndex(-1 , 10);
    }

    @Test
    public void Array_newInstance(){

        Person person = new Person();
        System.out.println(Objects.requireNonNull(person));
    }

    @Test
    public void ArrayList_removeAll_Collection(){
        ArrayList list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        ArrayList list2 = new ArrayList();
        list2.add(3);
        list.removeAll(list2);
        System.out.println(list);
    }
}
