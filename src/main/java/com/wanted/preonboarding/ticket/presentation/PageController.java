package com.wanted.preonboarding.ticket.presentation;

import com.wanted.preonboarding.core.config.UserAuth;
import com.wanted.preonboarding.ticket.application.TicketSeller;
import com.wanted.preonboarding.ticket.domain.dto.ReserveInfo;
import com.wanted.preonboarding.ticket.domain.dto.ReserveResult;
import com.wanted.preonboarding.user.application.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PageController {

    private UserInfoService userInfoService;
    private TicketSeller ticketSeller;

    @Autowired
    public PageController (UserInfoService userInfoService, TicketSeller ticketSeller){
        this.userInfoService = userInfoService;
        this.ticketSeller = ticketSeller;
    }

    /**
     * 예메 가능 공연 리스트 페이지
     * @return
     */
    @GetMapping("/performance")
    public String performances(){
        return "performance/performanceList";
    }

    /**
     * 공연 상세 정보
     * @param performanceId
     * @param model
     * @return
     */
    @GetMapping("/performance/{performanceId}")
    public String performanceDetail(@PathVariable String performanceId, Model model){
        model.addAttribute("performanceId", performanceId);
        return "performance/performanceDetail";
    }

    /**
     * 예약 시스템 페이지: GET
     * URL: 도메인/reservation/{performanceId}/{round}
     * @param performanceId
     * @param round
     * @param seat
     * @param line
     * @param userAuth
     * @param model
     * @return reservation/reservationPage.html
     */
    @GetMapping("/performance/{performanceId}/{round:[0-9]*}")
    public String reservationPage(@PathVariable String performanceId, @PathVariable int round, @RequestParam int seat, @RequestParam char line, @AuthenticationPrincipal
    UserAuth userAuth, Model model){
        model.addAttribute("userInfo", userInfoService.getUserAndPayments(userAuth.getUsername()));
        model.addAttribute("performanceInfo", ticketSeller.getPerformanceAndSeatInfo(performanceId, round, seat, line));

        return "reservation/reservationPage";
    }

    /**
     * 예약 시스템 페이지: POST
     * @param performanceId
     * @param round
     * @return
     */
    @PostMapping("/performance/{performanceId}/{round:[0-9]*}")
    public String reservationPage(@PathVariable String performanceId, @PathVariable int round, @ModelAttribute ReserveInfo reserveInfo, RedirectAttributes redirectAttributes){
        Integer reservationId = 0;
        boolean result = ticketSeller.reserve(reserveInfo, reservationId);
        System.out.println("RESULT IS " + result);
        System.out.println("reservationId IS " + reservationId);
        redirectAttributes.addAttribute("reservationId", reservationId);
        return "redirect:/reservation/{reservationId}";
    }

    /**
     * 예약 결과 정보 표시 페이지: GET
     * @param reservationId
     * @param model
     * @return
     */
    @GetMapping("/reservation/{reservationId}")
    public String reservationResultPage(@PathVariable int reservationId, Model model){
        ReserveResult reserveResult = ticketSeller.getReservationInfo(reservationId);
        model.addAttribute("reserveResult", reserveResult);
        return "reservation/reserveResult";
    }


}
