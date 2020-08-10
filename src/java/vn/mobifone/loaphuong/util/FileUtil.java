/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.primefaces.model.DefaultStreamedContent;
import vn.mobifone.loaphuong.lib.Session;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFilter;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ChienDX
 */
//Util xử lý file
public class FileUtil {

    public static final int BUFFER_SIZE = 65536; // 64K

    //Lấy đường dẫn vật lý
    public static String getRealPath(String strRelativePath) {
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return context.getRealPath(strRelativePath);
    }

    //Upload file tạm
    public static String uploadTempFile(byte[] file, String strFileName) throws Exception {
        FileUtil.deleteOldFile(FileUtil.getRealPath("/resources/tmp/"), "*.*", 2 * 60 * 60);
        return uploadFile(new ByteArrayInputStream(file), strFileName, false);
    }

    //Upload file tạm
    public static String uploadTempFile(UploadedFile file) throws Exception {
        FileUtil.deleteOldFile(FileUtil.getRealPath("/resources/tmp/"), "*.*", 2 * 60 * 60);
        return uploadFile(file.getInputstream(), file.getFileName(), false);
    }

    //Upload file
    public static String uploadFile(UploadedFile file) throws Exception {
        return uploadFile(file.getInputstream(), file.getFileName(), false);
    }

    //Upload file
    public static String uploadFile(InputStream is, String strFileName, boolean bReplace) throws Exception {
        String strFilePath = "";
        OutputStream out = null;

        try {
            forceFolderExist(FileUtil.getRealPath("/resources/tmp/"));

            if (!bReplace) {
                //get sessionid
                String strSessionId = Session.getSessionId();
                String strTimestamp = System.currentTimeMillis() + "";
                String strUnique = DigestUtils.md5Hex(strSessionId + strTimestamp);
                strFilePath = FileUtil.getRealPath("/resources/tmp") + "/" + strUnique + "_" + strFileName;

            } else {
                strFilePath = FileUtil.getRealPath("/resources/tmp/" + strFileName);
            }

            int read;
            byte[] bytes = new byte[1024];
            out = new FileOutputStream(strFilePath);

            while ((read = is.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

        } catch (Exception ex) {
            throw ex;

        } finally {
            is.close();
            if (out != null) {
                out.flush();
                out.close();
            }
        }

        return strFilePath;
    }

    //Tải file
    public static DefaultStreamedContent downloadFile(String strFileName, String strFilePath) throws Exception {
        String strContentType = FacesContext.getCurrentInstance().getExternalContext().getMimeType(strFileName);
        InputStream stream = new FileInputStream(new File(strFilePath));
        return new DefaultStreamedContent(stream, strContentType, strFileName);
    }

    //Tải file
    public static DefaultStreamedContent downloadFile(File file) throws Exception {
        return downloadFile(file.getName(), file.getPath());
    }

    //Tạo thư mục
    public static void forceFolderExist(String strFolder) throws IOException {
        File flTemp = new File(strFolder);
        if (!flTemp.exists()) {
            if (!flTemp.mkdirs()) {
                throw new IOException("Could not create folder " + strFolder);
            }
        } else if (!flTemp.isDirectory()) {
            throw new IOException("A file with name" + strFolder + " already exist");
        }
    }

    //Xóa file cũ
    public static void deleteOldFile(String strPath, String strWildcard, int iOffset) {
        if (!strPath.endsWith("/")) {
            strPath += "/";
        }
        File flFolder = new File(strPath);
        if (!flFolder.exists()) {
            return;
        }
        String strFileList[] = flFolder.list(new WildcardFilter(strWildcard));
        if (strFileList != null && strFileList.length > 0) {
            long lCurrentTime = (new java.util.Date()).getTime();
            for (int iFileIndex = 0; iFileIndex < strFileList.length; iFileIndex++) {
                File fl = new File(strPath + strFileList[iFileIndex]);
                if (lCurrentTime - fl.lastModified() >= iOffset) {
                    fl.delete();
                }
            }
        }
    }

    //Sao chép file
    public static boolean copyFile(String strSrc, String strDest) {
        FileInputStream isSrc = null;
        FileOutputStream osDest = null;
        try {
            File flDest = new File(strDest);
            if (flDest.exists()) {
                flDest.delete();
            }

            File flSrc = new File(strSrc);
            if (!flSrc.exists()) {
                return false;
            }

            isSrc = new FileInputStream(flSrc);
            osDest = new FileOutputStream(flDest);

            byte btData[] = new byte[BUFFER_SIZE];
            int iLength;
            while ((iLength = isSrc.read(btData)) != -1) {
                osDest.write(btData, 0, iLength);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            safeClose(isSrc);
            safeClose(osDest);
        }
    }

    //Xóa file
    public static boolean deleteFile(String strSrc) {
        File flSrc = new File(strSrc);
        return flSrc.delete();
    }

    //Close stream
    public static void safeClose(OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Close stream
    public static void safeClose(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Fix lỗi font khi xuất báo cáo html
    public static String convertExportFile(String strFilePath) throws Exception {
        BufferedReader br = null;
        BufferedWriter writer = null;

        try {
            //Create new file
            String strNewFile = FilenameUtils.getFullPath(strFilePath) + "/" + FilenameUtils.getBaseName(strFilePath) + "_converted." + FilenameUtils.getExtension(strFilePath);
            (new File(strNewFile)).createNewFile();

            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(strNewFile), "UTF-16LE"));
            writer.write("\uFEFF");

            //Read and write new file
            br = new BufferedReader(new InputStreamReader(new FileInputStream(strFilePath), "UTF8"));
            String line;

            while ((line = br.readLine()) != null) {
                writer.write(line);
            }
            return strNewFile;

        } catch (Exception ex) {
            throw ex;

        } finally {
            if (br != null) {
                br.close();
            }

            if (writer != null) {
                writer.close();
            }
        }
    }
}
