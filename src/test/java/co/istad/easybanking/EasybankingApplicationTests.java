package co.istad.easybanking;

import co.istad.easybanking.jwtGenerater.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

@SpringBootTest
class EasybankingApplicationTests {

    public JwtTokenService jwtTokenService;
	@Test
	void contextLoads() {
		BigDecimal amountBill = BigDecimal.valueOf(1110000);
		amountBill= amountBill.divide(BigDecimal.valueOf(4100),2, RoundingMode.HALF_UP);
		System.out.println(amountBill);
	}

}
