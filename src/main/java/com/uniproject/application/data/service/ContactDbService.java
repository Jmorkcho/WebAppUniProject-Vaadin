package com.uniproject.application.data.service;

import com.uniproject.application.data.entity.Contact;
import com.uniproject.application.data.repository.ContactRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactDbService {

    @Autowired
    private ContactRepository repository;

    public List<Contact> findByEmailAndOrName (String emailQuery, String nameQuery) {

//        try {
//            Session session = (Session) entityManager.getDelegate();
//            CriteriaBuilder cb = session.getCriteriaBuilder();
//            CriteriaQuery<Contact> cr = cb.createQuery(Contact.class);
//            Root<Contact> root = cr.from(Contact.class);

//            List<Predicate> predicates = new ArrayList<>();
//            if (!StringUtils.isBlank(emailQuery)) {
//                predicates.add(cb.like(root.get("EMAIL"), "%" + emailQuery + "%"));
//            }
//            if (!StringUtils.isBlank(nameQuery)) {
//                Predicate firstNamePredicate = cb.like(root.get("FIRST_NAME"), "%" + nameQuery + "%");
//                Predicate lastNamePredicate = cb.like(root.get("LAST_NAME"), "%" + nameQuery + "%");
//                Predicate namePredicate = cb.or(firstNamePredicate, lastNamePredicate);
//                predicates.add(namePredicate);
//            }
//            cr.select(root)
//                    .where(predicates.toArray(new Predicate[0]));


            if (!StringUtils.isBlank(emailQuery) && !StringUtils.isBlank(nameQuery))  {
                return repository.searchByNameAndEmail(nameQuery, emailQuery);
            } else if(!StringUtils.isBlank(emailQuery)) {
                return repository.searchByEmail(emailQuery);
            } else if(!StringUtils.isBlank(nameQuery)) {
                return repository.searchByName(nameQuery);
            } else {
                return repository.findAll();
            }
//            Query<Contact> query = session.createQuery(cr);
//            return query.getResultList();
//        } finally {
//            entityManager.close();
//        }
    }
}
