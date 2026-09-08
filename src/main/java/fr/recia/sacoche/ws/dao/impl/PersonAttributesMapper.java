/*
 * Copyright © 2026 GIP-RECIA (https://www.recia.fr/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.recia.sacoche.ws.dao.impl;

import fr.recia.sacoche.ws.model.Person;
import org.jspecify.annotations.NonNull;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;

import javax.naming.NamingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonAttributesMapper implements ContextMapper<Person> {

    private final String label;
    private final Pattern pattern;

    public PersonAttributesMapper(String label, Pattern pattern){
        this.label = label;
        this.pattern = pattern;
    }

    @Override
    public Person mapFromContext(@NonNull Object ctx) throws NamingException {
        DirContextAdapter context = (DirContextAdapter) ctx;
        String uid = context.getStringAttribute(LdapAttributes.UID);
        String sn = context.getStringAttribute(LdapAttributes.SN);
        String givenName = context.getStringAttribute(LdapAttributes.GIVEN_NAME);
        String[] externalIds = context.getStringAttributes(LdapAttributes.ESCO_PERSON_EXTERNAL_IDS);
        String extractedId = null;
        if(null != externalIds){
            Matcher matcher = null;
            for(String externalId : externalIds){
                matcher = this.pattern.matcher(externalId);
                if(matcher.matches()){
                    extractedId = matcher.group(1);
                    break;
                }
            }
        }
        if (null == extractedId){
            return null;
        }
        Person person = new Person();
        person.setUid(uid);
        person.setExternalId(extractedId);
        person.setProfile(this.label);
        person.setFirstName(givenName);
        person.setLastName(sn);
        return person;

    }
}
