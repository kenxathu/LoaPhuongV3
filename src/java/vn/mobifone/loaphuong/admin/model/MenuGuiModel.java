package vn.mobifone.loaphuong.admin.model;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package mobifone.marketplace.admin.model;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//import mobifone.marketplace.admin.entity.Module;
//import vn.mobifone.loaphuong.lib.SystemLogger;
//import vn.mobifone.loaphuong.lib.data.DataPreprocessor;
//
///**
// *
// * @author ChienDX
// */
////Xử lý dữ liệu cho menu
//public class MenuGuiModel extends DataPreprocessor implements Serializable {
//
//    //Lấy danh sách menu
//    public List<Module> getListModule() throws Exception {
//        List<Module> returnValue = new ArrayList<Module>();
//
//        try {
//            open();
//            String strSQL = "SELECT id module_id,   name  module_name, "
//                    + " TYPE,  parent_id,  action_url, '' icon,  \n" 
//                    + " REPLACE (SUBSTR (SYS_CONNECT_BY_PATH (name, '/'), 2),'/',\n" 
//                    + " '||') breadcrumb     \n" 
//                    + " FROM ACTION a  \n" 
//                    + " WHERE a.status = 1   CONNECT BY PRIOR a.id = a.parent_id  \n" 
//                    + " START WITH a.parent_id is null ORDER SIBLINGS BY a.name";
//
//            mStmt = mConnection.prepareStatement(strSQL);
//            mRs = mStmt.executeQuery();
//
//            while (mRs.next()) {
//                Module tmp = new Module();
//                tmp.setModuleId(mRs.getLong("module_id"));
//                tmp.setModuleName(mRs.getString("module_name"));
//                tmp.setType(mRs.getString("TYPE"));
//                tmp.setParentId(mRs.getLong("parent_id"));
//                tmp.setActionUrl(mRs.getString("action_url"));
//                tmp.setIcon(mRs.getString("icon"));
//                tmp.setBreadCrumb(mRs.getString("breadcrumb"));
//                returnValue.add(tmp);
//            }
//
//            return returnValue;
//
//        } catch (Exception ex) {
//            SystemLogger.getLogger().error(ex);
//            throw ex;
//
//        } finally {
//            close(mRs);
//            close(mStmt);
//            close();
//        }
//    }
//}
