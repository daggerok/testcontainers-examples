package io.github.daggerok.testcontainerslifecycleexamples;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SpringBootTestcontainersIntegrationTests {

	@Test
	void should_test_context() {
	}
}
