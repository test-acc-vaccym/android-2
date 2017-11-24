package com.edroplet.qxx.saneteltabactivity;

import com.edroplet.qxx.saneteltabactivity.utils.sscanf.Sscanf;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by qxs on 2017/11/23.
 */
public class SscanfTest {
    @Test
    public void scanf_isCorrect(){
        String a = "zhu";
        Object b = new Object();
        Object bs[] =  Sscanf.scan("wo shi ren", "wo shi %s",b);
        a = String.valueOf(bs[0]);
        assertEquals(null,"ren",a);
    }
    @Test
    public void scanfFloat_isCorrect(){
        float a;
        float b = 0;
        Object bs[] =  Sscanf.scan("wo shi ren, wo de qian shi 12.009", "wo shi ren, wo de qian shi %f",b);
        System.out.println(bs[0]);
        a = Float.parseFloat(String.valueOf(bs[0]));
        System.out.println(a);
        assertEquals(12.009,a,0.01);
    }


    /**
     * @param args
     */
    public static void main(String[] args) {

        // note: the 'output' variables ("1", "2", "3") are for sanity check-only. They don't
        // actually do anything other than tell the util what data type it should be looking for
        Object variables[] = Sscanf.scan("123a 45bc", "123%02c 45%c%.25c678", '1', '2', '3');

        System.out.println("parse count: " + variables.length);
        System.out.println("char1: " + (char)variables[0]);
        System.out.println("char2: " + (char)variables[1]);
        System.out.println("char3: " + (char)variables[2]);

        // output:
        // parse count: 3
        // char1: a
        // char2: b
        // char3: c

        variables = Sscanf.scan("This is only,attest", "%s %s,%04s", "1", "2", "3");

        System.out.println("parse count: " + variables.length);
        System.out.println("str1: " + (String)variables[0]);
        System.out.println("str2: " + (String)variables[1]);
        System.out.println("str3: " + (String)variables[2]);

        // output:
        // parse count: 3
        // str1: This
        // str2: is only
        // str3: atte

        variables = Sscanf.scan("my hex string: DEADBEEF\n", "my hex string: %X\n", 1);

        System.out.println("parse count: " + variables.length);
        System.out.println("hex str1: " + (int)variables[0]);

        // Output:
        // parse count: 1
        // hex str1: 3735928559

        variables = Sscanf.scan("BSJB: 00DE 00AD", "BSJB: %X %04X", 1, 2);

        System.out.println("parse count: " + variables.length);
        System.out.println("hex str1: " + (int)variables[0]);
        System.out.println("4 char hex str2: " + (int)variables[1]);

        // Output:
        // parse count: 2
        // hex str1: 222
        // 4 char hex str2: 173

        variables = Sscanf.scan("My int: 12345, My long: -23432, My fixed int: -2344, My unsigned: -2344",
                "My int: %i, My long: %d, My fixed int: %04i, My unsigned: -%u",(int) 1, 2, (int)3, 4);

        System.out.println("parse count: " + variables.length);
        System.out.println("int: " + (int)variables[0]);
        System.out.println("long: " + (int)variables[1]);
        System.out.println("fixed int: " + (int)variables[2]);
        System.out.println("unsigned: " + (int)variables[3]);

        // Output:
        // parse count: 4
        // int: 12345
        // long: -23432
        // fixed int: -2344
        // unsigned: 2344

        Object var[] = {1, 2};
        String buffer = "005 DEAD\n";
        if(Sscanf.scan2(buffer, "%X %x", var) !=2) {
            System.err.println("error parsing hex strings");
        } else {
            System.out.println("hex1: " + (int)var[0]);
            System.out.println("hex2: " + (int)var[1]);
        }
    }
}