package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.CommentDAO;
import edu.piotrjonski.scrumus.dao.IssueDAO;
import edu.piotrjonski.scrumus.dao.RetrospectiveDAO;
import edu.piotrjonski.scrumus.domain.Comment;
import edu.piotrjonski.scrumus.domain.Issue;
import edu.piotrjonski.scrumus.domain.Retrospective;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;

@Stateless
public class CommentManager {

    @Inject
    private CommentDAO commentDAO;

    @Inject
    private IssueDAO issueDAO;

    @Inject
    private RetrospectiveDAO retrospectiveDAO;

    public Optional<Comment> addCommentToIssue(Comment comment, Issue issue) {
        if (issueDAO.exist(issue.getId())) {
            Optional<Comment> savedComment = commentDAO.saveOrUpdate(comment);
            issue.addComment(savedComment.get());
            issueDAO.saveOrUpdate(issue);
            return savedComment;
        }
        return Optional.empty();
    }

    public Optional<Comment> addCommentToRetrospective(Comment comment, Retrospective retrospective) {
        if (retrospectiveDAO.exist(retrospective.getId())) {
            Optional<Comment> savedComment = commentDAO.saveOrUpdate(comment);
            retrospective.addComment(savedComment.get());
            retrospectiveDAO.saveOrUpdate(retrospective);
            return savedComment;
        }
        return Optional.empty();
    }

    public void removeCommentFromIssue(Comment comment, Issue issue) {
        if (issueDAO.exist(issue.getId())) {
            issue.removeComment(comment);
            issueDAO.saveOrUpdate(issue);
        }
    }

    public void removeCommentFromRetrospective(Comment comment, Retrospective retrospective) {
        if (retrospectiveDAO.exist(retrospective.getId())) {
            retrospective.removeComment(comment);
            retrospectiveDAO.saveOrUpdate(retrospective);
        }
    }

}
