package co.istad.easybanking;

import co.istad.easybanking.jwtGenerater.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EasybankingApplicationTests {

    public JwtTokenService jwtTokenService;
	@Test
	void contextLoads() {
		/*String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuilder sb = new StringBuilder(9);

		// Generate 9 random characters
		for (int i = 0; i < 9; i++) {
			// Append a random character from CHARACTERS string
			sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
		}
		System.out.println("FT"+sb.toString());*/
		//String fundToken = jwtTokenService.generateFundToken(fundTokenMap);
	}

}
