package edu.zyh.finalproject.service.Impl;

import edu.zyh.finalproject.POJO.Auction;
import edu.zyh.finalproject.POJO.AuctionDetail;
import edu.zyh.finalproject.dao.AuctionDao;
import edu.zyh.finalproject.service.AuctionService;
import edu.zyh.finalproject.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zyhsna
 */
@Service
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    private AuctionDao auctionDao;

    @Autowired
    private TaskService taskService;

    @Override
    public Auction queryAuctionByTaskId(int taskId) {
        Auction auction = auctionDao.queryAuctionByTaskId(taskId);
        return auction;
    }

    @Override
    public AuctionDetail queryAuctionDetail(int auctionId, int userId) {
        int result = auctionDao.queryAuctionDetailExistence(auctionId, userId);
        if (result == 0) {
            //用户未出价过
            return null;
        } else {
            AuctionDetail auctionDetail = auctionDao.queryAuctionDetailByAuctionIdAndUserId(auctionId, userId);
            return auctionDetail;
        }
    }

    @Override
    public int createAuctionDetail(AuctionDetail auctionDetail) {
        int detail = auctionDao.createAuctionDetail(auctionDetail);
        int i = auctionDao.increaseBidderNum(auctionDetail.getAuctionId());
        return detail;
    }

    @Override
    public int updateBid(AuctionDetail auctionDetail) {
        int result = auctionDao.queryAuctionDetailExistence(auctionDetail.getAuctionId(), auctionDetail.getBidderId());
        int updateBid = -2;
        if (result == 1) {
            //有出价则更新
            updateBid = auctionDao.updateBid(auctionDetail);
        } else {
            //没有则新建
            updateBid = auctionDao.createAuctionDetail(auctionDetail);
            auctionDao.increaseBidderNum(auctionDetail.getAuctionId());
        }
        return updateBid;
    }

    @Override
    public int selectWinner(int auctionId, int bidderId) {
        int selectWinner = auctionDao.selectWinner(auctionId, bidderId);
        return selectWinner;
    }

    @Override
    public int closeAuction(int auctionId) {
        int i = auctionDao.closeAuctionByTaskId(auctionId);
        return i;
    }

    @Override
    public List<AuctionDetail> queryBidder(int auctionId) {
        List<AuctionDetail> auctionDetails = auctionDao.queryBidderByAuctionId(auctionId);
        return auctionDetails;
    }


    @Override
    public int createAuction(Auction newAuction) {
        int result = -2;
        try {
            result = auctionDao.createAuction(newAuction);
        } catch (Exception e) {
            return -1;
        }
        return result;
    }

    @Override
    public int bidderParticipated(int auctionId, int userId) {
        int i = auctionDao.bidderParticipate(auctionId, userId);
        if (i == 1) {
            Auction auction = auctionDao.queryAuctionByAuctionId(auctionId);
            int i1 = taskService.receiveTask(auction.getTaskId(), userId);
            if (i1 == 1) {
                return 1;
            } else if (i1 == 2) {
                return 2;
            }
        }
        return -1;
    }
}
