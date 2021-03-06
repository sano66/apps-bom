
# Spring Security 学習プロジェクト

自分用のSpring Security の学習です。

## 仕様

### 認証シナリオ

1. シングルサインオン経由でアクセスするウェブアプリケーションを想定
1. シングルサインオンはユーザ名、パスワードで認証済の前提
1. シングルサインオン認証が済んでいる場合はhttpヘッダーにSSO_USERが付与されている
1. ウェブアプリケーションを利用するにはシングルサインオンの認証とは別に、店識別とSSO_USERで指定したユーザ名による認証が必要
1. アプリケーションの認証ができなかった場合は、店識別とユーザ名による認証が必要
1. アプリケーションの認証が未済のとき、店識別をリクエストパラメータで指定した場合は、認証済のSSO_USERとあわせてログイン認証を実施する
1. 店識別とはNYとかPARISとか店が存在する地名を表す任意の文字列
1. ログイン済のユーザは別の店識別のユーザへ切り替え可能

### Spring Securityが具備すべきフィルタ

1. リクエストヘッダーのSSO_USERの有無を判断し、ない場合はエラーとするフィルター
1. 店識別とユーザ名によるフォームログインのフィルター
1. 認証未済の場合、店識別が与えられた場合に認証を行うフィルター
1. 店識別とユーザ名によるユーザ切り替えのフィルター

### Spring Security設定仕様

1. /assets, /staticはHTMLリソースのため認証対象外
1. デバッグ機能は利用可能とし、ロギング設定ファイル/src/resources/logback.xmlにより出力を制御する
1. DB認証のデータソースは認証専用のデータソースを利用する
1. ログアウトの際、セッションを初期化する
1. 認証エラー、認可エラーに関してエラーページは用意しない

### おまけ

1. デバッグのため/debug.jsp, /permit_all.htmlは誰でもアクセス可能にする
1. アプリケーションのログインフォームはJSPではなくサーブレットで作成し、認証・認可のパッケージの可搬性を高める

## 実装

### Step 1 認証対象外のページの設定、リクエストヘッダーをチェックするフィルターの設定

1. デバッグのため/debug.jsp, /permit_all.htmlは誰でもアクセス可能にする
1. リクエストヘッダーをチェックするフィルタの定義

#### point

1. configure(WebSecurity)で認証対象外のページを設定する
1. シングルサインオン認証が済んでいるかどうかを、httpヘッダーのSSO_USERの有無で判断するフィルタを作成する
1. configure(HttpSecurity)でリクエストヘッダーをチェックするフィルタを登録する
1. 上記フィルタで使用するtokenに応じた認証プロバイダの登録
1. フィルタが設定するtoken.detailはGrantedAuthorityを実装していること

### Step 2 店識別とユーザ名によるフォームログインのフィルターの実装

#### point

1. PreAuthenticatedAuthenticationProvider#authenticate()でAnonymousTokenを返却させ次のフィルタを有効にさせる
1. Configurerでフォームログインフィルタを設定する
1. MyAuthenticationFilter#attemptAuthentication()で認証未済トークン作成
1. MyAuthenticationProvider#support()でトークンとプロバイダのひも付け
1. MyAuthenticationProvider#createSuccessAuthentication()で認証済トークン作成
1. 複数項目によるloadUserByArgs()を備えるUserDetailsService相当のインターフェースを作成

### Step 3 認証未済の場合、店識別が与えられた場合に認証を行うフィルターの実装

#### point

1. MyPreAuthenticationFilterをフォームログイン前に設定
1. MyPreAuthenticationFilter#requiresAuthentication()で認証未済の場合の判断
1. MyPreAuthenticationFilter#attemptAuthentication()でトークン作成
1. 認証プロバイダはMyAuthenticationProviderを利用

### Step 4 店識別とユーザ名によるユーザ切り替えのフィルターの実装

#### point

1. SwitchUserFilterをextendしてMySwitchUserFilterを作成
1. 認証のUserDetailsServiceはダミーを設定し、複数項目による認証サービスMyUserDetailsServiceをimplementする。
1. 認証プロバイダはMyAuthenticationProviderを利用

# 利用規約

誤りがあれば遠慮なくご指摘ください。