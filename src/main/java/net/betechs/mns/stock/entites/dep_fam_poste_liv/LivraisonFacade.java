/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.betechs.mns.stock.entites.dep_fam_poste_liv;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author JAFFAR
 */
@Stateless
public class LivraisonFacade extends AbstractFacade<Livraison> {

    @PersistenceContext(unitName = "us.pastec_MNS_STOCK_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LivraisonFacade() {
        super(Livraison.class);
    }
    
}
