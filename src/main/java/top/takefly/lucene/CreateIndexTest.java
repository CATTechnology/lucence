package top.takefly.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.w3c.dom.Text;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @program: Demo1
 * @description: 创建索引
 * @author: 戴灵飞
 * @create: 2019-07-19 20:45
 **/
public class CreateIndexTest {

    private Directory directory;

    private IndexWriter indexWriter;

    private IndexReader indexReader;
    private IndexSearcher indexSearcher;


    @Before
    public void init() throws Exception {
        //1.创建Directory
        directory = FSDirectory.open(new File("D:\\javaEE\\luke\\index").toPath());
        //2.根据directory来创建IndexWriter
        indexWriter = new IndexWriter(directory, new IndexWriterConfig(new IKAnalyzer()));
        //3.根据directory来创建
//        indexReader = DirectoryReader.open(directory);
        //4.创建indexSearch
//        indexSearcher = new IndexSearcher(indexReader);
    }


    @Test
    public void createIndex() throws Exception {
        //3.根据indexWriter来加载document
        File files = new File("H:\\18年javaEE课件\\18JAVA课件\\87.lucene\\lucene\\lucen资料\\searchsource");
        if (!files.isDirectory()) {
            System.out.println("这不是一个文件夹....,你让我怎么分析！！！");
            throw new RuntimeException("没文件我实在没法分析!!!");
        }
        File[] fileArr = files.listFiles();
        for (File file : fileArr) {
            //1.创建document
            Document document = new Document();
            //2.获取名称
            String fileName = file.getName();
            //3.获取文件路径
            String filePath = file.getPath();
            //4.读取文件
            String fileString = FileUtils.readFileToString(file, "UTF-8");
            //5.获取文件大小
            long fileSize = FileUtils.sizeOf(file);
            //6.创建document
            Field nameField = new TextField("fileName", fileName, Field.Store.YES);
            LongPoint sizePoint = new LongPoint("size", fileSize);
            StoredField sizeField = new StoredField("size", fileSize);
            StoredField contentField = new StoredField("path", filePath);
            Field pathField = new TextField("content", fileString, Field.Store.YES);
            document.add(nameField);
            document.add(sizePoint);
            document.add(sizeField);
            document.add(contentField);
            document.add(pathField);

            //加入indexWriter
            indexWriter.addDocument(document);
        }
        //6.关闭写入流
        indexWriter.close();
    }


    @Test
    public void queryIndex() throws Exception {
        Query query = new TermQuery(new Term("content", "lucene"));
        //执行查询
        TopDocs topDocs = indexSearcher.search(query, 10);
        //遍历结果
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            //获取document对象
            Document document = indexSearcher.doc(scoreDoc.doc);
            //获取文件
            System.out.println("文件名称:" + document.get("fileName"));
            System.out.println("文件大小:" + document.get("size"));
            System.out.println("文件路径:" + document.get("path"));
            System.out.println("文件内容:" + document.get("content"));

            System.out.println("______________________________________");
        }
    }

    @Test
    public void QueryFileName() throws Exception {
        Query query = new TermQuery(new Term("fileName", "新添加的文档"));
        TopDocs topDocs = indexSearcher.search(query, 100);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("内容"+document.get("content"));
            System.out.println("------------------------------");
        }

    }

    @Test
    public void rangeQueryTest() throws Exception {
        //范围查询
        Query query = LongPoint.newRangeQuery("size", 0, 1000);
        //
        TopDocs search = indexSearcher.search(query, 20);
        System.out.println("搜索到的结果:"+search.totalHits);
        for (ScoreDoc scoreDoc : search.scoreDocs) {
            //获取document
            System.out.println("---------------------文件开始----------------------");
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("fileName:" + document.get("fileName"));
            System.out.println("size:" + document.get("size"));
            System.out.println("path:" + document.get("path"));
            System.out.println("content:" + document.get("content"));

            System.out.println("---------------------文件结束----------------------\n\n");
        }

    }

    @Test
    public void queryParserTest() throws Exception {
        QueryParser queryParser = new QueryParser("content", new IKAnalyzer());
        Query query = queryParser.parse("lucene是java编写的最好的工具");
        printResult(query);
    }

    @Test
    public void queryParserTest2() throws Exception{
        QueryParser queryParser = new QueryParser("content", new IKAnalyzer());
        Query query = queryParser.parse("lucene是java编写的最好的工具");
        TopDocs topDocs = indexSearcher.search(query, 20);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("文件名称:"+document.get("fileName"));
            System.out.println("文件大小:"+document.get("size"));
            System.out.println("文件路径:"+document.get("path"));
            System.out.println("文件内容:"+document.get("content"));
            System.out.println("---------------------文件结束-----------------\n\n");
        }
    }

    public void printResult(Query query) throws Exception {
        TopDocs search = indexSearcher.search(query, 20);
        for (ScoreDoc scoreDoc : search.scoreDocs) {
            //获取document
            System.out.println("---------------------文件开始----------------------");
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("fileName:" + document.get("fileName"));
            System.out.println("size:" + document.get("size"));
            System.out.println("path:" + document.get("path"));
            System.out.println("content:" + document.get("content"));

            System.out.println("---------------------文件结束----------------------\n\n");
        }
    }

    /**
     * 删除索引
     *
     * @throws Exception
     */
    @Test
    public void deleteIndex() throws Exception {
        //indexWriter.deleteAll();
        indexWriter.deleteDocuments(new TermQuery(new Term("filename", "添加")));
        indexWriter.commit();
    }

    @Test
    public void deleteIndex2() throws Exception{
        indexWriter.deleteDocuments(new TermQuery(new Term("fileName" , "添加")));
        indexWriter.commit();
    }


    @Test
    public void addDocument() throws Exception {
        Document document = new Document();
        document.add(new TextField("fileName", "新添加的文档", Field.Store.YES));
        document.add(new TextField("content", "新添加的文档的内容", Field.Store.NO));
        document.add(new StoredField("path", "d:/temp/1.txt"));
        //LongPoint创建索引
        document.add(new LongPoint("size", 1000L));
//        document.add(new TextField("fileSize", "1000" , Field.Store.YES));
        //StoreField存储数据
        document.add(new StoredField("size", 1000L));

        //添加到索引库
        indexWriter.addDocument(document);

        indexWriter.close();
    }

    /**
     * 修改document
     *
     * @throws Exception
     */
    @Test
    public void updateDocument() throws Exception {
        //创建document对象
        Document document = new Document();
        //添加属性
        document.add(new TextField("fileName", "要更新的文档", Field.Store.YES));
        document.add(new LongPoint("size", 1000));
        document.add(new StoredField("size", 1000));
        document.add(new StoredField("path", "D:/temp:/a.txt"));
        document.add(new TextField("content", " Lucene 简介 Lucene 是一个基于 Java 的全文信息检索工具包," +
                "它不是一个完整的搜索应用程序,而是为你的应用程序提供索引和搜索功能。",
                Field.Store.YES));
        long l = indexWriter.updateDocument(new Term("fileName", "新添加的文档"), document);
        //indexWriter.updateDocument(new Term("fileName" , "要更新的文档"))
        System.out.println(l);
        //关闭写入流
        indexWriter.close();
    }
}
