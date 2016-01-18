package edu.piotrjonski.scrumus.services;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectKeyGeneratorTest {

    @Test
    public void shouldGenerateProjectKeySameAsName() {
        // given
        String projectName = "name";
        String expectedProjectKey = projectName.toUpperCase();
        ProjectKeyGenerator projectKeyGenerator = new ProjectKeyGenerator();

        // when
        String result = projectKeyGenerator.generateProjectKey(projectName);

        // then
        assertThat(result).isEqualTo(expectedProjectKey);
    }

    @Test
    public void shouldTrimToEightDigits() {
        // given
        String projectName = "LongProjectName";
        String expectedProjectKey = projectName.toUpperCase()
                                               .substring(0, 8);
        ProjectKeyGenerator projectKeyGenerator = new ProjectKeyGenerator();

        // when
        String result = projectKeyGenerator.generateProjectKey(projectName);

        // then
        assertThat(result).isEqualTo(expectedProjectKey);
    }

    @Test
    public void shouldReturnEmptyStringIfProjectNameIsNull() {
        // given
        String projectName = null;
        String expectedProjectKey = "";
        ProjectKeyGenerator projectKeyGenerator = new ProjectKeyGenerator();

        // when
        String result = projectKeyGenerator.generateProjectKey(projectName);

        // then
        assertThat(result).isEqualTo(expectedProjectKey);
    }


}