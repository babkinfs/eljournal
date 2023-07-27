package com.babkin.eljournal.service;

import com.babkin.eljournal.entity.working.Call;
import com.babkin.eljournal.repo.CallRepos;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CallService {
    //24.03.2021

    private CallRepos callRepos;

    public CallService(CallRepos callRepos){
        this.callRepos = callRepos;
    }

    public Call findByName(String name){
        return callRepos.findByName( name );
    }

    public List<Call> findAll(){
        return callRepos.findAll();
    }

    public Call findCallById(Long callid){
        return callRepos.findCallById( callid );
    }

    public void delete(Call call){
        callRepos.delete( call );
    }

    public Call update(Long callId, String name){
        Call fromDB = callRepos.findCallById(callId);
        if (fromDB != null){
            fromDB.setName(name);
            return callRepos.saveAndFlush(fromDB);
        }
        return null;
    }
}
