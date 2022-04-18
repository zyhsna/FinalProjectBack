package edu.zyh.finalproject.controller;

import edu.zyh.finalproject.POJO.Auction;
import edu.zyh.finalproject.POJO.AuctionDetail;
import edu.zyh.finalproject.service.AuctionService;
import edu.zyh.finalproject.util.JSONData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zyhsna
 */
@RestController
@RequestMapping("/api/v1/auction")
public class AuctionController {
    @Autowired
    private AuctionService auctionService;

    @RequestMapping("/createAuction")
    public JSONData createAuction(@RequestBody Auction newAuction) {
        int result = auctionService.createAuction(newAuction);
        return result > 0 ? JSONData.buildSuccess() : JSONData.buildError(32, "创建拍卖失败");
    }

    @RequestMapping("/queryAuctionByTaskId")
    public JSONData queryAuctionByTaskId(int taskId) {
        Auction auction = auctionService.queryAuctionByTaskId(taskId);
        return auction != null ? JSONData.buildSuccess(auction) : JSONData.buildError(33, "获取拍卖信息失败");
    }

//    @RequestMapping("/queryAuctionDetail")
//    public JSONData queryAuctionDetail(int taskId, int userId) {
//        AuctionDetail auctionDetail = auctionService.queryAuctionDetail(taskId, userId);
//        return auctionDetail != null ? JSONData.buildSuccess(auctionDetail) : JSONData.buildError(34, "获取拍卖详情失败");
//
//    }

    @RequestMapping("/queryAuctionDetail")
    public JSONData queryAuctionDetail(int auctionId, int userId) {
        AuctionDetail auctionDetail = auctionService.queryAuctionDetail(auctionId, userId);
        return auctionDetail != null ? JSONData.buildSuccess(auctionDetail) : JSONData.buildError(34, "获取拍卖详情失败");

    }



    @RequestMapping("/createAuctionDetail")
    public JSONData createAuctionDetail(@RequestBody AuctionDetail auctionDetail) {
        int result = auctionService.createAuctionDetail(auctionDetail);
        return result > 0 ? JSONData.buildSuccess() : JSONData.buildError(35, "出价失败");
    }

    @RequestMapping("/updateBid")
    public JSONData updateBid(@RequestBody AuctionDetail auctionDetail) {
        int result = auctionService.updateBid(auctionDetail);
        return result > 0 ? JSONData.buildSuccess() : JSONData.buildError(36, "更新报价失败");
    }

    @RequestMapping("/selectWinner")
    public JSONData selectWinner(int auctionId, int bidderId) {
        int result = auctionService.selectWinner(auctionId, bidderId);
        return result > 0 ? JSONData.buildSuccess() : JSONData.buildError(37, "选择赢家失败失败");
    }


    @RequestMapping("/closeAuction")
    public JSONData closeAuction(int auctionId) {
        int result = auctionService.closeAuction(auctionId);
        return result > 0 ? JSONData.buildSuccess() : JSONData.buildError(38, "关闭竞拍失败");
    }

    @RequestMapping("/queryBidder")
    public JSONData queryBidder(int auctionId) {
        List<AuctionDetail> result = auctionService.queryBidder(auctionId);
        return result !=null ? JSONData.buildSuccess(result) : JSONData.buildError(39, "查询竞拍者失败");

    }

    @RequestMapping("/bidderParticipate")
    public JSONData bidderParticipate(int auctionId, int bidderId) {
        int result = auctionService.bidderParticipated(auctionId,bidderId);
        return result ==1 ? JSONData.buildSuccess(result) : JSONData.buildError(40, "参加任务失败");

    }
}
