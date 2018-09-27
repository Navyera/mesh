package com.linkedin.backend.admin.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "Users")
public class AdminXML {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "User")
    private List<AppUserXML> users;

    public AdminXML(List<AppUserXML> users) {
        this.users = users;
    }

    public List<AppUserXML> getUsers() {
        return users;
    }

    public void setUsers(List<AppUserXML> users) {
        this.users = users;
    }
}
