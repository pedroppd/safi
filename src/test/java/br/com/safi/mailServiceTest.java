package br.com.safi;

import br.com.safi.services.MailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class mailServiceTest {

	@Autowired
	private MailService mailService;

	@Test
	void buildMailBody() {
		String userName = "Fulano";
		String targetLink = "https://www.google.com/";
		String content = this.mailService.buildEmailBody(userName, targetLink);
		boolean isValid = content.contains(userName) && content.contains(targetLink);
		Assertions.assertTrue(isValid);
	}

}
