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

    @Test
    public void shouldReturnFirstFourLettersOfEachWord() {
        // given
        String projectName = "test test";
        String expectedProjectKey = "TETE";
        ProjectKeyGenerator projectKeyGenerator = new ProjectKeyGenerator();

        // when
        String result = projectKeyGenerator.generateProjectKey(projectName);

        // then
        assertThat(result).isEqualTo(expectedProjectKey);
    }

    @Test
    public void shouldReturnFirstTwoLettersOfEachWordWhenThreeWordProjectName() {
        // given
        String projectName = "test test test";
        String expectedProjectKey = "TETETE";
        ProjectKeyGenerator projectKeyGenerator = new ProjectKeyGenerator();

        // when
        String result = projectKeyGenerator.generateProjectKey(projectName);

        // then
        assertThat(result).isEqualTo(expectedProjectKey);
    }

    @Test
    public void shouldReturnFirstTwoLettersOfEachWordWhenFourWordProjectName() {
        // given
        String projectName = "test test test test";
        String expectedProjectKey = "TETETETE";
        ProjectKeyGenerator projectKeyGenerator = new ProjectKeyGenerator();

        // when
        String result = projectKeyGenerator.generateProjectKey(projectName);

        // then
        assertThat(result).isEqualTo(expectedProjectKey);
    }

    @Test
    public void shouldReturnFirstLetterOfEachWordWhenMoreThanFourWordProjectName() {
        // given
        String projectName = "test test test test test";
        String expectedProjectKey = "TTTTT";
        ProjectKeyGenerator projectKeyGenerator = new ProjectKeyGenerator();

        // when
        String result = projectKeyGenerator.generateProjectKey(projectName);

        // then
        assertThat(result).isEqualTo(expectedProjectKey);
    }

    @Test
    public void shouldReturnEmptyStringWhenProjectNameIsEmpty() {
        // given
        String projectName = "    ";
        String expectedProjectKey = "";
        ProjectKeyGenerator projectKeyGenerator = new ProjectKeyGenerator();

        // when
        String result = projectKeyGenerator.generateProjectKey(projectName);

        // then
        assertThat(result).isEqualTo(expectedProjectKey);
    }


}