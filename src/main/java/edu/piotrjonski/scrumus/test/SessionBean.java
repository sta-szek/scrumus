package edu.piotrjonski.scrumus.test;

import edu.piotrjonski.scrumus.model.*;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Singleton
public class SessionBean {

    @Inject
    private EntityManager entityManager;

    public void putsmthtoDB() {
        String projectKey = "PRKEY";
        String projectName = "Project Name";
        String sprintName1 = "Sprint 1";
        String sprintName2 = "Sprint 2";
        TimeRange timeRange1 = new TimeRange(LocalDateTime.now(),
                                             LocalDateTime.now()
                                                          .plusDays(7));
        TimeRange timeRange2 = new TimeRange(LocalDateTime.now()
                                                          .plusDays(7),
                                             LocalDateTime.now()
                                                          .plusDays(14));
        String storyName1 = "Story name 1";
        String storyName2 = "Story name 2";
        String issueSummary1 = "Issue summary 1";
        String issueSummary2 = "Issue summary 2";
        String issueSummary3 = "Issue summary 3";
        String issueSummary4 = "Issue summary 4";
        String description1 = "Description1";
        String description2 = "Description2";
        String description3 = "Description3";
        String description4 = "Description4";

        // --------- ISSUES
        Issue issue1 = new Issue(0, projectKey + "0", issueSummary1, description1);
        Issue issue2 = new Issue(0, projectKey + "1", issueSummary2, description2);
        Issue issue3 = new Issue(0, projectKey + "2", issueSummary3, description3);
        Issue issue4 = new Issue(0, projectKey + "3", issueSummary4, description4);
        List<Issue> issues1 = new ArrayList<>();
        List<Issue> issues2 = new ArrayList<>();
        issues1.add(issue1);
        issues1.add(issue2);
        issues2.add(issue3);
        issues2.add(issue4);

        // --------- STORY
        Story story1 = new Story(0, storyName1, 5, issues1);
        Story story2 = new Story(0, storyName2, 3, issues2);
        List<Story> stories1 = new ArrayList<>();
        List<Story> stories2 = new ArrayList<>();
        stories1.add(story1);
        stories2.add(story2);

        // --------- SPRINTY
        Sprint sprint1 = new Sprint(0, sprintName1, timeRange1, stories1);
        Sprint sprint2 = new Sprint(0, sprintName2, timeRange2, stories2);
        List<Sprint> sprints = new ArrayList<>();
        sprints.add(sprint1);
        sprints.add(sprint2);

        // --------- PROJEKTY
        Project project = new Project(0, projectKey, projectName, sprints);

        // --------- BACKLOGI
        List<Issue> backlogIssues1 = new ArrayList<>();
        List<Issue> backlogIssues2 = new ArrayList<>();
        backlogIssues1.add(issue1);
        backlogIssues2.add(issue3);
        Backlog backlog1 = new Backlog(0, backlogIssues1);
        Backlog backlog2 = new Backlog(0, backlogIssues2);


        List<Sprint> sprints1 = new ArrayList<>();
        List<Story> stories3 = new ArrayList<>();
        stories3.add(new Story(0, "name", 10, createIssues(100)));
        sprints1.add(new Sprint(0, "name", new TimeRange(LocalDateTime.now(), LocalDateTime.now()), stories3));
        Project project1 = new Project(0, "p2", "p2", sprints1);
        entityManager.persist(project1);
        // entityManager.persist(backlog1);
        // entityManager.persist(backlog2);
    }

    public Project getProjectEntity() {
        Query query = entityManager.createNamedQuery(Project.FIND_BY_ID);
        query.setParameter(Project.FIND_BY_ID_PARAMETER, 1);

        return (Project) query.getSingleResult();

    }

    private List<Issue> createIssues(int amount) {
        List<Issue> issues = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            issues.add(new Issue(0,
                                 "IssueKey-" + i,
                                 "Issue summary",
                                 "Very long descriptionVery long descriptionVery long descriptionVery long " +
                                 "descriptionVery long descriptionVery long descriptionVery long descriptionVery long" +
                                 " descriptionVery long descriptionVery long descriptionVery long descriptionVery " +
                                 "long descriptionVery long descriptionVery long descriptionVery long descriptionVery" +
                                 " long descriptionVery long descriptionVery long descriptionVery long " +
                                 "descriptionVery long descriptionVery long descriptionVery long descriptionVery long" +
                                 " descriptionVery long descriptionVery long descriptionVery long descriptionVery " +
                                 "long descriptionVery long descriptionVery long descriptionVery long description"));
        }
        return issues;
    }

}
