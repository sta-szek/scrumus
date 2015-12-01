package edu.piotrjonski.scrumus.dao;


import edu.piotrjonski.scrumus.dao.model.project.IssueTypeEntity;
import edu.piotrjonski.scrumus.domain.IssueType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IssueTypeDAOTest {

    private IssueTypeDAO issueTypeDAO = new IssueTypeDAO();

    @Test
    public void shouldMapToDatabaseModel() {
        // given
        int id = 1;
        String issueTypeName = "issueTypeName";
        IssueType issueType = new IssueType();
        issueType.setId(id);
        issueType.setName(issueTypeName);

        // when
        IssueTypeEntity result = issueTypeDAO.mapToDatabaseModel(issueType);

        // then
        assertThat(result.getName()).isEqualTo(issueTypeName);
        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    public void shouldMapToDomainModel() {
        // given
        int id = 1;
        String issueTypeName = "issueTypeName";
        IssueTypeEntity issueType = new IssueTypeEntity();
        issueType.setId(id);
        issueType.setName(issueTypeName);

        // when
        IssueType result = issueTypeDAO.mapToDomainModel(issueType);

        // then
        assertThat(result.getName()).isEqualTo(issueTypeName);
        assertThat(result.getId()).isEqualTo(id);
    }
}