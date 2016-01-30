package edu.piotrjonski.scrumus.ui.services;

import edu.piotrjonski.scrumus.business.CommentManager;
import edu.piotrjonski.scrumus.domain.Comment;
import lombok.Data;
import org.slf4j.Logger;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;

@Data
public class CommentService implements Serializable {

    @Inject
    private transient Logger logger;

    @Inject
    private CommentManager commentManager;

    private String commentBody;

    public void addCommentToIssue() {
        if (commentIsEmpty()) {
            return;
        }
        try {
            int userIntId = Integer.parseInt(getRequestParameter("userId"));
            int issueIntId = Integer.parseInt(getRequestParameter("issueId"));
            Comment comment = createCommentFromField(userIntId);
            commentManager.addCommentToIssue(comment, issueIntId);
            logger.info("Created comment for issue with id '" + issueIntId + "' by user with id '" + userIntId + "'.");
        } catch (NumberFormatException e) {
            return;
        }
    }

    public void addCommentToRetrospective() {
        if (commentIsEmpty()) {
            return;
        }
        try {
            int userIntId = Integer.parseInt(getRequestParameter("userId"));
            int retrospectiveIntId = Integer.parseInt(getRequestParameter("retrospectiveId"));
            Comment comment = createCommentFromField(userIntId);
            commentManager.addCommentToRetrospective(comment, retrospectiveIntId);
            logger.info("Created comment for retrospective with id '" + retrospectiveIntId + "' by user with id '" + userIntId + "'.");
        } catch (NumberFormatException e) {
            return;
        }
    }

    private Comment createCommentFromField(final int userIntId) {
        Comment comment = new Comment();
        comment.setCommentBody(commentBody);
        comment.setDeveloperId(userIntId);
        clearFields();
        return comment;
    }

    private void clearFields() {
        commentBody = null;
    }

    private boolean commentIsEmpty() {return commentBody == null || commentBody.isEmpty();}

    private String getRequestParameter(String parameter) {
        return FacesContext.getCurrentInstance()
                           .getExternalContext()
                           .getRequestParameterMap()
                           .get(parameter);
    }
}
