package service;

import dao.StagingDAO;

public class StagingService {
    private static StagingService instance;

    public static StagingService getInstance() {
        if (instance == null)
            instance = new StagingService();
        return instance;
    }
     public void insertRecord(String[] params){
         StagingDAO.getInstance().insertRecord(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], Double.parseDouble(params[11]), params[12]);
     }
    public void transfer_from_staging(){
        StagingDAO.getInstance().transfer_from_staging();
    }
}
