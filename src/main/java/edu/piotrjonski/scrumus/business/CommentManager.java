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

    public void addCommentToIssue(Comment comment, int issueId) {
        Optional<Issue> issue = issueDAO.findById(issueId);
        if (issue.isPresent()) {
            addCommentToIssue(comment, issue.get());
        }
    }

    public Optional<Comment> addCommentToIssue(Comment comment, Issue issue) {
        if (issueExist(issue)) {
            Optional<Comment> savedComment = saveCommentAndAddToIssue(comment, issue);
            return savedComment;
        }
        return Optional.empty();
    }

    public Optional<Comment> addCommentToRetrospective(Comment comment, Retrospective retrospective) {
        if (retrospectiveExist(retrospective)) {
            Optional<Comment> savedComment = saveCommentAndAddToRetrospective(comment, retrospective);
            return savedComment;
        }
        return Optional.empty();
    }

    public void removeCommentFromIssue(Comment comment, Issue issue) {
        if (issueExist(issue)) {
            issue.removeComment(comment);
            issueDAO.saveOrUpdate(issue);
        }
    }

    public void removeCommentFromRetrospective(Comment comment, Retrospective retrospective) {
        if (retrospectiveExist(retrospective)) {
            retrospective.removeComment(comment);
            retrospectiveDAO.saveOrUpdate(retrospective);
        }
    }

    public void removeCommentFromIssue(final int commentIntId, final int issueIntId) {
        Optional<Comment> comment = commentDAO.findById(commentIntId);
        Optional<Issue> issue = issueDAO.findById(issueIntId);
        if (comment.isPresent() && issue.isPresent()) {
            removeCommentFromIssue(comment.get(), issue.get());
        }
    }

    private Optional<Comment> saveCommentAndAddToRetrospective(final Comment comment, final Retrospective retrospective) {
        Optional<Comment> savedComment = commentDAO.saveOrUpdate(comment);
        retrospective.addComment(savedComment.get());
        retrospectiveDAO.saveOrUpdate(retrospective);
        return savedComment;
    }

    private Optional<Comment> saveCommentAndAddToIssue(final Comment comment, final Issue issue) {
        Optional<Comment> savedComment = commentDAO.saveOrUpdate(comment);
        issue.addComment(savedComment.get());
        issueDAO.saveOrUpdate(issue);
        return savedComment;
    }

    private boolean issueExist(final Issue issue) {return issueDAO.exist(issue.getId());}

    private boolean retrospectiveExist(final Retrospective retrospective) {return retrospectiveDAO.exist(retrospective.getId());}

}
