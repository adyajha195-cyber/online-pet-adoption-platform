package backend;

import java.sql.SQLException;

import database.AdoptionApplicationDAO;
import model.AdoptionApplication;

/** Application logic for adoption applications, built on AdoptionApplicationDAO. */
public class AdoptionApplicationServiceImpl implements AdoptionApplicationService {

    private final AdoptionApplicationDAO dao;

    public AdoptionApplicationServiceImpl() {
        this(new AdoptionApplicationDAO());
    }

    public AdoptionApplicationServiceImpl(AdoptionApplicationDAO dao) {
        this.dao = dao;
    }

    @Override
    public boolean submitApplication(int adopterId, int petId) {
        return submitApplication(adopterId, petId, "");
    }

    @Override
    public boolean submitApplication(int adopterId, int petId, String applicationDetails) {
        try {
            return dao.addApplication(adopterId, petId, applicationDetails) > 0;
        } catch (SQLException e) {
            throw new ServiceException("Could not submit application", e);
        }
    }

    @Override
    public boolean updateApplicationStatus(int applicationId, String status) {
        try {
            return dao.updateApplicationStatus(applicationId, status);
        } catch (IllegalArgumentException e) {
            return false; // invalid status string
        } catch (SQLException e) {
            throw new ServiceException("Could not update application status", e);
        }
    }

    @Override
    public String getApplicationStatus(int applicationId) {
        try {
            AdoptionApplication a = dao.getApplicationById(applicationId);
            return (a == null) ? null : a.getStatus();
        } catch (SQLException e) {
            throw new ServiceException("Could not read application status", e);
        }
    }
}
