package com.uniproject.application.data.service;


import com.uniproject.application.data.entity.Company;
import com.uniproject.application.data.entity.Contact;
import com.uniproject.application.data.entity.Status;
import com.uniproject.application.data.repository.CompanyRepository;
import com.uniproject.application.data.repository.ContactRepository;
import com.uniproject.application.data.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrmService {

    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
    private final StatusRepository statusRepository;
    private final ContactDbService contactDbService;

    public CrmService(ContactRepository contactRepository,
                      CompanyRepository companyRepository,
                      StatusRepository statusRepository,
                      ContactDbService contactDbService) {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
        this.statusRepository = statusRepository;
        this.contactDbService = contactDbService;
    }

    public List<Contact> findAllContacts(String emailFilter, String nameFilter) {
        return  contactDbService.findByEmailAndOrName(emailFilter, nameFilter);
    }


//    public List<Contact> findAllContactsByEmail(String stringFilter2) {
//        if (StringUtils.isBlank(stringFilter2)) {
//            return contactRepository.findAll();
//        } else {
//            return contactRepository.search(stringFilter2);
//        }
//    }

    public long countContacts() {
        return contactRepository.count();
    }

    public void deleteContact(Contact contact) {
        contactRepository.delete(contact);
    }

    public void saveContact(Contact contact) {
        if (contact == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        contactRepository.save(contact);
    }

    public void deleteCompany(Company company) {
        companyRepository.delete(company);
    }

    public void saveCompany(Company company) {
        if (company == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        companyRepository.save(company);
    }

    public void deleteStatus(Status status ) {
        statusRepository.delete(status);
    }

    public void saveStatus(Status status) {
        if (status == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        statusRepository.save(status);
    }


    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    public List<Status> findAllStatuses(){
        return statusRepository.findAll();
    }
}
