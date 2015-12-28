package edu.piotrjonski.scrumus.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleTest {

    @Test
    public void shouldAddUserToRole() {
        // given
        Role role = new Role();
        Developer developer = new Developer();

        // when
        role.addDeveloper(developer);

        // then
        assertThat(role.getDevelopers()).contains(developer);
    }

    @Test
    public void shouldRemoveUserFromRole() {
        // given
        Role role = new Role();
        Developer developer = new Developer();
        role.addDeveloper(developer);

        // when
        role.removeDeveloper(developer);

        // then
        assertThat(role.getDevelopers()).doesNotContain(developer);
    }
}