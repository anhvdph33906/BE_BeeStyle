package com.datn.beestyle.service.scheduler;

import com.datn.beestyle.dto.promotion.PromotionResponse;
import com.datn.beestyle.entity.Promotion;
import com.datn.beestyle.entity.Voucher;
import com.datn.beestyle.enums.DiscountStatus;
import com.datn.beestyle.repository.ProductVariantRepository;
import com.datn.beestyle.repository.PromotionRepository;
import com.datn.beestyle.repository.VoucherRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class StatusUpdateScheduler {
    private final PromotionRepository promotionRepository;
    private final VoucherRepository voucherRepository;
    private final ProductVariantRepository productVariantRepository;

    public StatusUpdateScheduler(PromotionRepository promotionRepository, VoucherRepository voucherRepository, ProductVariantRepository productVariantRepository) {
        this.promotionRepository = promotionRepository;
        this.voucherRepository = voucherRepository;
        this.productVariantRepository = productVariantRepository;
    }

    @Scheduled(cron = "0 */30 * * * ?") // Chạy mỗi giờ
    public void updateStatuses() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        List<Promotion> promotions = promotionRepository.findAll();
        for (Promotion promotion : promotions) {
            if (currentTimestamp.before(promotion.getStartDate())) {
                promotion.setStatus(DiscountStatus.UPCOMING.getValue());
            } else if (currentTimestamp.after(promotion.getEndDate())) {
                promotion.setStatus(DiscountStatus.EXPIRED.getValue());
            } else {
                promotion.setStatus(DiscountStatus.ACTIVE.getValue());
            }
        }
        promotionRepository.saveAll(promotions);


        List<Voucher> vouchers = voucherRepository.findAll();
        for (Voucher voucher : vouchers) {
            if (currentTimestamp.before(voucher.getStartDate())) {
                voucher.setStatus(DiscountStatus.UPCOMING.getValue());
            } else if (currentTimestamp.after(voucher.getEndDate())) {
                voucher.setStatus(DiscountStatus.EXPIRED.getValue());
            } else {
                voucher.setStatus(DiscountStatus.ACTIVE.getValue());
            }
        }
        voucherRepository.saveAll(vouchers);
    }
    @Scheduled(cron = "0 */30 * * * ?") // Chạy mỗi giờ
    public void checkAndExpirePromotions() {

        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        List<PromotionResponse> endedPromotions = promotionRepository.findEndedPromotions(currentTimestamp);

        for (PromotionResponse promotionResponse : endedPromotions) {
            productVariantRepository.updateProductVariantToNullByPromotionId(promotionResponse.getId());
        }
    }
}
