package com.auc.web.handler;

import com.auc.dao.Client;

public abstract class TokenHandler {

  abstract public String handle(Client client, String userName, String sourceCode);
}
