class SessionsController < ApplicationController

  def new
    logged_in?
  end

  def create
    logout_keeping_session!
    user = User.authenticate(params[:login].downcase, params[:password])
    if user
      # Protects against session fixation attacks, causes request forgery
      # protection if user resubmits an earlier form using back
      # button. Uncomment if you understand the tradeoffs.
      # reset_session
      self.current_user = user
      new_cookie_flag = (params[:remember_me] == "1")
      handle_remember_cookie! new_cookie_flag
      redirect_back_or_default('/categories')
      flash[:notice] = "Witamy w serwisie."
    else
      note_failed_signin
      @login       = params[:login]
      @remember_me = params[:remember_me]
      render :action => 'new'
    end
  end


  def destroy
    logout_killing_session!
    flash[:notice] = "Wylogowano z serwisu."
    redirect_back_or_default('/')
  end


  def default
    if logged_in?
      redirect_to :action => :index, :controller => :categories
    else
      redirect_to login_url
    end
  end

  
  protected
  # Track failed login attempts
  def note_failed_signin
    flash[:error] = "Nie można zalogować: '#{params[:login]}'"
    logger.warn "Failed login for '#{params[:login]}' from #{request.remote_ip} at #{Time.now.utc}"
  end
end
