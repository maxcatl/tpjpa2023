package fr.istic.jpa;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Date;
import java.util.List;

public class JpaTest {


	private EntityManager manager;

	public JpaTest(EntityManager manager) {
		this.manager = manager;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EntityManager manager = EntityManagerHelper.getEntityManager();

		JpaTest test = new JpaTest(manager);

		EntityTransaction tx = manager.getTransaction();
		tx.begin();
		try {
			test.createInitialRdvs();
			test.listRdvs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		tx.commit();

			
   	 manager.close();
		EntityManagerHelper.closeEntityManagerFactory();
		System.out.println(".. done");
	}


	public void createInitialRdvs()
	{
		int numOfRdvs = manager.createQuery("Select a From Professional a", Professional.class).getResultList().size();
		if (numOfRdvs == 0)
		{
			addRdvs();
		}
	}


	public void addRdvs()
	{
		Professional pro1 = new Professional("hammond", "john");
		Client client1 = new Client("iencli", "Marc");
		Professional pro2 = new Professional("roy", "killian");
		RDV rdv1 = new RDV(new Date(), pro1, client1);
		RDV rdv2 = new RDV(new Date(System.currentTimeMillis() + 60000), pro2, client1);
		manager.persist(rdv1);
		manager.persist(rdv2);
	}


	public void listRdvs()
	{
		List<Professional> listRdvs = manager.createQuery("Select a From Professional a", Professional.class).getResultList();
		System.out.println("number of rdvs : " + listRdvs.size());
		for (Professional p : listRdvs)
		{
			System.out.println(p);
		}
	}


}
