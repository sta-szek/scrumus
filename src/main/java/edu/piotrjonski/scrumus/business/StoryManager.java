package edu.piotrjonski.scrumus.business;

import edu.piotrjonski.scrumus.dao.StoryDAO;
import edu.piotrjonski.scrumus.domain.Story;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class StoryManager {

    @Inject
    private StoryDAO storyDAO;

    @Inject
    private PermissionManager permissionManager;

    public Story createStory(Story story) throws AlreadyExistException {
        if (storyExist(story)) {
            throw new AlreadyExistException("Story już istnieje");
        }
        return storyDAO.saveOrUpdate(story)
                       .get();
    }

    public Story findStory(int storyId) {
        return storyDAO.findById(storyId)
                       .orElse(new Story());
    }

    public List<Story> findAllStoriesForSprint(int sprintId) {
        return storyDAO.findStoriesForSprint(sprintId);
    }


    private boolean storyExist(Story story) {
        return storyDAO.exist(story.getId());
    }
}
