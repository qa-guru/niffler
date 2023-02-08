package niffler.data.jpa.query;

import niffler.data.DataBase;
import niffler.data.jpa.EmfContext;
import niffler.data.jpa.JpaService;
import niffler.data.model.FriendsEntity;
import niffler.data.model.dao.FriendsDAO;


public class PostgresHibernateFriends extends JpaService implements FriendsDAO {
    public PostgresHibernateFriends() {
        super(EmfContext.INSTANCE.getEmf(DataBase.USERDATA).createEntityManager());
    }

    @Override
    public int addFriends(FriendsEntity friendsEntity) {
        persist(friendsEntity);
        return 0;
    }

    @Override
    public void updateFriends(FriendsEntity friendsEntity) {
        merge(friendsEntity);

    }

    @Override
    public void remove(FriendsEntity friendsEntity) {
        remove(friendsEntity);

    }


    @Override
    public FriendsEntity getByFriendsName(String name) {
        return em.createQuery("select u from FriendsEntity u where u.name=:name", FriendsEntity.class)
                .setParameter("name", name)
                .getSingleResult();
    }
}
