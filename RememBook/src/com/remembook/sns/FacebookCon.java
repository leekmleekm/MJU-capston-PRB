package com.remembook.sns;

import com.remembook.R;
import com.remembook.sns.*;
import com.remembook.sns.Facebook.DialogListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FacebookCon extends Activity implements View.OnClickListener
{
  private Facebook mFacebook = new Facebook(C.FACEBOOK_APP_ID);
  private Button mBtnLogin, mBtnFeed, mBtnLogout;
  private EditText mEtContent;//

  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.sns_main);
    
    mEtContent = (EditText) findViewById(R.id.etContent);//

    
    mBtnLogin = (Button) findViewById(R.id.btnLogin);
    mBtnFeed = (Button) findViewById(R.id.btnFeed);
    mBtnLogout = (Button) findViewById(R.id.btnLogout);
    
    mBtnLogin.setOnClickListener(this);
    mBtnFeed.setOnClickListener(this);
    mBtnLogout.setOnClickListener(this);
  }
  
  // ��ư�� OnClick �̺�Ʈ ó�� 
  @Override
  public void onClick(View v)
  {
    switch(v.getId())
    {
      case R.id.btnLogin: // Facebook login
        login();
        break;
      case R.id.btnFeed:  // Facebook�� �۾���
        feed();
        break;
      case R.id.btnLogout: // Facebook logout
        logout();
        break;
      default:
        break;
    }
  }
  
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    
    if (resultCode == RESULT_OK)
    {
      if (requestCode == C.FACEBOOK_AUTH_CODE)
      {
        mFacebook.authorizeCallback(requestCode, resultCode, data);
      }
    }
    else
    {
      if (requestCode == C.FACEBOOK_AUTH_CODE)
      {
        mFacebook.authorizeCallback(requestCode, resultCode, data);
      }      
    }
  }
  
  private void login()
  {
    mFacebook.authorize2(this, new String[] {"publish_stream, user_photos, email"}, new AuthorizeListener());
  }
  
  private void feed()
  {
    try
    {
      Log.v(C.LOG_TAG, "access token : " + mFacebook.getAccessToken());
      
      Bundle params = new Bundle();
      params.putString("message", mEtContent.getText().toString());
      params.putString("name", "����ڸ�");
      params.putString("link", "");
      params.putString("description", "FacebookCon������ ����Ʈ��.");
      params.putString("picture", "");

      mFacebook.request("me/feed", params, "POST");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }


  
  private void logout()
  {
    try
    {
      mFacebook.logout(this);      
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  
  // Facebook ������ ó���� ���� callback class
  public class AuthorizeListener implements DialogListener
  {
    @Override
    public void onCancel()
    {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void onComplete(Bundle values)
    {
      // TODO Auto-generated method stub
      if (C.D) Log.v(C.LOG_TAG, "::: onComplete :::");
    }

    @Override
    public void onError(DialogError e)
    {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void onFacebookError(FacebookError e)
    {
      // TODO Auto-generated method stub
      
    }
  }
}

