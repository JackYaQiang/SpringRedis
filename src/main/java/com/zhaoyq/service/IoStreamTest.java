package com.zhaoyq.service;

import com.zhaoyq.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 对象序列化和反序列化的运用
 */
public class IoStreamTest {
    private final static Logger logger = LoggerFactory.getLogger(IoStreamTest.class);
    public static void main(String[]args) {
        File file =new File("D:\\obj.txt");
        try {
           // writeStream(file);
            InputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对象的序列化，将需要写入到文件中的信息通过输出流的形式写到文件中
     * @param f
     * @throws IOException
     */
    public static void writeStream(File f) throws IOException {
        UserEntity userEntity =new UserEntity();
        userEntity.setName("赵亚强");
        FileOutputStream fileOutputStream =new FileOutputStream(f);
        ObjectOutputStream objectOutputStream =new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(userEntity);
        objectOutputStream.close();

    }

    /**
     * 通过输入流的形式将二进制流从文件中反序列化读取出来。。
     * @param f
     * @throws IOException
     */
    public static  void InputStream(File f) throws IOException{
        FileInputStream fileInputStream =new FileInputStream(f);
        ObjectInputStream objectInputStream =new ObjectInputStream(fileInputStream);
        try {
           UserEntity user= (UserEntity) objectInputStream.readObject();
            logger.info(user.getName());
            objectInputStream.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
