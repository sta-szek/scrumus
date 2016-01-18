package edu.piotrjonski.scrumus.dao.model.security;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleTypeTest {

    @Test
    public void shouldReturnAdmin() {
        // given

        // when
        String result = RoleType.ADMIN.getName();

        // then
        assertThat(result).isEqualTo("ADMIN");
    }

    @Test
    public void shouldReturnDeveloper() {
        // given

        // when
        String result = RoleType.DEVELOPER.getName();

        // then
        assertThat(result).isEqualTo("DEVELOPER");
    }

    @Test
    public void shouldReturnScrumMaster() {
        // given

        // when
        String result = RoleType.SCRUM_MASTER.getName();

        // then
        assertThat(result).isEqualTo("SCRUM_MASTER");
    }

    @Test
    public void shouldReturnProductOwner() {
        // given

        // when
        String result = RoleType.PRODUCT_OWNER.getName();

        // then
        assertThat(result).isEqualTo("PRODUCT_OWNER");
    }

}