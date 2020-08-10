/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author cuong.trinh
 */
public class MD5Encoder {
      	public static String encrypt(String inp) throws NoSuchAlgorithmException{

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(inp.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}
 public static void main(String[] args) {
        try {
            System.out.println(new MD5Encoder().encrypt("c514be4d-b6ee-4b22-bf6d-dbc90bb3afe1" + "5cff1615e99bbe27e6a6e40b" + "1533523698753"));
            System.out.println(new MD5Encoder().encrypt("PItfc58e9m3JtvhD9oe11g=="));
        } catch (Exception ex) {
            //Logger.getLogger(MD5Encoder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
