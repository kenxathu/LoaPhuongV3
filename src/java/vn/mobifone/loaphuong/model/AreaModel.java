/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.model;

import vn.mobifone.loaphuong.entity.AreaTree;
import vn.mobifone.loaphuong.entity.HouseHold;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import vn.mobifone.loaphuong.action.Action;
import vn.mobifone.loaphuong.admin.entity.MenuAuthorizator;

import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.lib.data.DataPreprocessor;
import vn.mobifone.loaphuong.util.SQLUtil;

/**
 *
 * @author cuong.trinh
 */
public class AreaModel extends DataPreprocessor implements Serializable{
    
    
    
    /**
     * Lấy danh sách area đang hoạt động status = 1
     *
     * @return List - Products
     * @author thont
     * @throws java.lang.Exception
     */
    ////////////////////////////////////////////////////////
//    public List<AreaTree> getListArea() throws Exception {
//        List<AreaTree> returnValue = new ArrayList<>();
//        try {
//            open();
//            String strSQL = ""
//                    + "SELECT a.ID, "
//                    + "       a.CODE, "
//                    + "       a.NAME, "
//                    + "       a.STATUS, "
//                    + "       a.CREATED_USER, "
//                    + "       a.CREATED_DATE, "
//                    + "       a.PARENT_ID, "
//                    + "       a.AREA_TYPE_ID "
//                    + "  FROM AREA a" ;
//
//            mStmt = mConnection.prepareStatement(strSQL);
//            mRs = mStmt.executeQuery();
//
//            while (mRs.next()) {
//                AreaTree entity = new AreaTree();
//                entity.setAreaId(mRs.getLong("ID"));
//                entity.setAreaCode(mRs.getString("CODE"));
//                entity.setAreaName(mRs.getString("NAME"));
//                entity.setStatus(mRs.getInt("STATUS"));
//                entity.setCreatedUser(mRs.getString("CREATED_USER"));
//                entity.setParentAreaId(mRs.getLong("PARENT_ID"));                
//                entity.setAreaType(mRs.getInt("AREA_TYPE_ID"));
//                returnValue.add(entity);
//
//            }
//        } finally {
//            close(mConnection, mStmt, mRs);
//            close();
//        }
//        return returnValue;
//    }
//    
//    
//    
//    // Lấy danh sách địa bàn theo cấp quản lý
//    //
//    public List<AreaTree> getListAreaByAreaId(long areaId) throws Exception {
//        List<AreaTree> returnValue = new ArrayList<>();
////        AreaTree root = new AreaTree();
//        try {
//            open();
////            
////            String strSQL = ""
////                    + "SELECT a.ID, "
////                    + "       a.CODE, "
////                    + "       a.NAME, "
////                    + "       a.STATUS, "
////                    + "       a.CREATED_USER, "
////                    + "       a.CREATED_DATE, "
////                    + "       a.PARENT_ID, "
////                    + "       a.AREA_TYPE_ID "
////                    + "  FROM AREA a"
////                    + "  Where id = ? ";
////
////            mStmt = mConnection.prepareStatement(strSQL);
////            mStmt.setLong(1, areaId);
////            mRs = mStmt.executeQuery();
////
////            while (mRs.next()) {
////                
////                root.setAreaId(mRs.getLong("ID"));
////                root.setAreaCode(mRs.getString("CODE"));
////                root.setAreaName(mRs.getString("NAME"));
////                root.setStatus(mRs.getInt("STATUS"));
////                //root.setCreatedUser(mRs.getString("CREATED_USER"));
////                root.setParentAreaId(mRs.getLong("PARENT_ID"));                
////                root.setAreaType(mRs.getInt("AREA_TYPE_ID"));
////                returnValue.add(root);
////            }
////            
//            
////            strSQL = ""
////                    + "WITH tree (id, parent_id, CODE, name, STATUS, CREATED_USER, area_type_id ) as "
////                    + "(SELECT id, parent_id, 0 as area_type_id, name, CODE, STATUS, CREATED_USER "
////                    + "FROM area "
////                    + "WHERE parent_id = ? "
////                    + "UNION ALL SELECT c2.id, c2.parent_id, tree.area_type_id + 1, c2.name , c2.CODE, c2.STATUS, c2.CREATED_USER "
////                    + "FROM area c2 "
////                    + "INNER JOIN tree ON tree.id = c2.parent_id) "
////                    + "SELECT * FROM tree";
//
//
//
//            String strSQL = ""
//            + "WITH tree (id, parent_id, area_type_id, CODE, name, STATUS, CREATED_USER ) as "
//            + "( SELECT id, parent_id, area_type_id, CODE, name, STATUS, CREATED_USER "
//            + "  FROM area "
//            + "  WHERE id = ? "
//            + "  UNION ALL "
//            + " SELECT c2.id, c2.parent_id, c2.area_type_id, c2.CODE, c2.name, c2.STATUS, c2.CREATED_USER "
//            + " FROM area c2 "
//            + " INNER JOIN tree ON tree.id = c2.parent_id ) "
//            + "SELECT * FROM tree" ;
//        
//            mStmt = mConnection.prepareStatement(strSQL);
//            mStmt.setLong(1, areaId);
//            mRs = mStmt.executeQuery();
//
//            while (mRs.next()) {
//                AreaTree entity = new AreaTree();
//                entity.setAreaId(mRs.getLong("ID"));
//                entity.setAreaCode(mRs.getString("CODE"));
//                entity.setAreaName(mRs.getString("NAME"));
//                entity.setStatus(mRs.getInt("STATUS"));
//                entity.setCreatedUser(mRs.getString("CREATED_USER"));
//                entity.setParentAreaId(mRs.getLong("PARENT_ID"));                
//                entity.setAreaType(mRs.getInt("AREA_TYPE_ID"));
//                returnValue.add(entity);
//
//            }
//            
//            
//        } catch (Exception e) {
//            
//        } finally {
//            close(mConnection, mStmt, mRs);
//            close();
//        }
//        return returnValue;
//
//       
//    }
    
        /**
     * Thêm Area
     *
     * @param AreaTree
     * @return long - sequence
     * @author cuong.trinh
     * @throws java.lang.Exception
     */
    ////////////////////////////////////////////////////////
//    public boolean addArea(AreaTree area) throws Exception {
//        boolean result = true;
//        try {
//            open();
//            mConnection.setAutoCommit(false);
//
//            
//            String strSQL = ""
//                    + "INSERT INTO AREA (ID, "
//                    + "                  CODE, "
//                    + "                  NAME, "
//                    + "                  STATUS, "
//                    + "                  CREATED_USER, "
//                    + "                  CREATED_DATE, "
//                    + "                  PARENT_ID, "
//                    + "                  AREA_TYPE_ID) "
//                    + "     VALUES (?, "
//                    + "             ?, "
//                    + "             ?, "
//                    + "             ?, "
//                    + "             ?, "
//                    + "             SYSDATE, "
//                    + "             ?, "
//                    + "             ?)";
//
//            long iID = SQLUtil.getSequenceValue(mConnection, "SEQ_AREA");
//            mStmt = mConnection.prepareStatement(strSQL);
//            mStmt.setLong(1, iID);
//            mStmt.setString(2, area.getAreaCode());
//            mStmt.setString(3, area.getAreaName());
//            mStmt.setInt(4, area.getStatus());
//            mStmt.setString(5, area.getCreatedUser());
//            mStmt.setLong(6, area.getParentAreaId());
//            mStmt.setInt(7, area.getAreaType());
//            mStmt.executeUpdate();
//            mConnection.commit();
//            
//
//        } catch (Exception ex) {
//            result =  false;
//            mConnection.rollback();
//            SystemLogger.getLogger().error(ex);
//            throw new Exception(ex);
//        } finally {
//            close(mStmt);
//            close(mRs);
//            close();
//        }
//        return result;
//    }
//    
//    
//            /**
//     * Sửa hộ gd     *
//     * @param AreaTree
//     * @return long - sequence
//     * @author cuong.trinh
//     * @throws java.lang.Exception
//     */
//    ////////////////////////////////////////////////////////
//    public boolean updateArea(AreaTree area) throws Exception {
//        boolean result = true;
//        try {
//            open();
//
//            mConnection.setAutoCommit(false);
//            String strSQL
//                    = " UPDATE AREA "
//                    + " SET CODE = ?, NAME=? " 
//                    + " WHERE ID = ? ";
//            mStmt = mConnection.prepareStatement(strSQL);
//            mStmt.setString(1, area.getAreaCode());
//            mStmt.setString(2, area.getAreaName());
//            mStmt.setLong(3, area.getAreaId());
//
//            mStmt.executeUpdate();
//            mConnection.commit();
//        } catch (Exception ex) {
//            result = false;
//            mConnection.rollback();
//            SystemLogger.getLogger().error(ex);
//            throw new Exception(ex);
//
//        } finally {
//            close(mRs);
//            close(mStmt);
//            close();
//        }
//        return result;
//    }
//     
//     public boolean deleteArea(AreaTree area) throws Exception {
//        boolean result = true;
//        try {
//            open();
//
//            mConnection.setAutoCommit(false);
//            String strSQL
//                    = " DELETE FROM AREA "
//                    + " WHERE ID = ? ";
//            mStmt = mConnection.prepareStatement(strSQL);
//            mStmt.setLong(1, area.getAreaId());
//
//            mStmt.executeUpdate();
//            mConnection.commit();
//        } catch (Exception ex) {
//            result = false;
//            mConnection.rollback();
//            SystemLogger.getLogger().error(ex);
//            throw new Exception(ex);
//
//        } finally {
//            close(mRs);
//            close(mStmt);
//            close();
//        }
//        return result;
//    }
//     
//     
         /**
     * Lấy danh sách products đang hoạt động status = 1
     *
     * @return List - Products
     * @author thont
     * @throws java.lang.Exception
     */
    ////////////////////////////////////////////////////////
//    public List<HouseHold> getListHouseByArea(long areaId) throws Exception {
//        List<HouseHold> returnValue = new ArrayList<>();
//        try {
//            open();
//            String strSQL = ""
//                    + "SELECT ID, "
//                    + "       HOLDER_NAME, "
//                    + "       ADDRESS, "
//                    + "       AREA_ID, "
//                    + "       AREA_PATH "
//                    + "  FROM HOUSEHOLD " 
//                    + "  WHERE AREA_ID =  ?";
//
//            mStmt = mConnection.prepareStatement(strSQL);
//            mStmt.setLong(1, areaId);
//            mRs = mStmt.executeQuery();
//
//            while (mRs.next()) {
//                HouseHold entity = new HouseHold();
//                entity.setId(mRs.getLong("ID"));
//                entity.setHolder_name(mRs.getString("HOLDER_NAME"));
//                entity.setAddress(mRs.getString("ADDRESS"));
//                entity.setArea_id(mRs.getLong("AREA_ID"));
//    
//                returnValue.add(entity);
//
//            }
//        } finally {
//            close(mConnection, mStmt, mRs);
//            close();
//        }
//        return returnValue;
//    }
    
    
    
    
//    ////////////////////////////////////////////////////////
//    public List<MenuAuthorizator> getListMenuAuthorizato(long roleId) throws Exception {
//        List<MenuAuthorizator> returnValue = new ArrayList<>();
//        try {
//            open();
//            String strSQL = ""
//                    + "SELECT a.ACTION_URL "
//                    + "  FROM ACTION a, ROLE_ACTION b" 
//                    + "  WHERE a.ID = b.ACTION_ID and b.ROLE_ID = ?";
//
//            mStmt = mConnection.prepareStatement(strSQL);
//            mStmt.setLong(1, roleId);
//            mRs = mStmt.executeQuery();
//
//            while (mRs.next()) {
//                MenuAuthorizator entity = new MenuAuthorizator();
//                entity.setModulePath(mRs.getString("ACTION_URL"));
//                returnValue.add(entity);
//
//            }
//        } finally {
//            close(mConnection, mStmt, mRs);
//            close();
//        }
//        return returnValue;
//    }
    
    
    
//    public List<Action> getListAction() throws Exception {
//        List<Action> returnValue = new ArrayList<>();
//        try {
//            open();
//            String strSQL = "SELECT id, name, TYPE, status, action_url, parent_id, "
//                            +" REPLACE (SUBSTR (SYS_CONNECT_BY_PATH (name, '/'), 2), " 
//                            +                               " '/', "
//                            +                               " '||') "
//                            +                         "  breadcrumb  "
//                            + " FROM action a "
//                            + " WHERE a.status = 1 "
//                            + "CONNECT BY PRIOR a.id = a.parent_id  "
//                            + "START WITH a.id = 1 ";
//
//            mStmt = mConnection.prepareStatement(strSQL);
//            mRs = mStmt.executeQuery();
//
//            while (mRs.next()) {
//                Action entity = new Action();
//                entity.setActionId(mRs.getLong("ID"));
//                entity.setActionName(mRs.getString("Name"));
//                entity.setActionUrl(mRs.getString("ACTION_URL"));
//                entity.setParentId(mRs.getLong("PARENT_ID"));
//                entity.setStatus(mRs.getInt("STATUS"));
//                entity.setType(mRs.getString("TYPE"));
//                entity.setBreadCrumb(mRs.getString("breadcrumb"));
//                returnValue.add(entity);
//
//            }
//        } finally {
//            close(mConnection, mStmt, mRs);
//            close();
//        }
//        return returnValue;
//    }
    
}
