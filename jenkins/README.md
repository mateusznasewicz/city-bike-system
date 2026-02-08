# 🛠️ Jenkins Manual Configuration

**Jenkins URL:** [http://192.168.56.200:8080](http://192.168.56.200:8080)

After the automated setup is complete, perform these manual steps to configure external access and notifications.

---

## 1. 🔑 Database Credentials
*Required for smoke tests and Swarm secrets creation.*

1. Navigate to: `Manage Jenkins` -> `Credentials` -> `System` -> `Global credentials (unrestricted)` -> `+ Add Credentials`.
2. Configure the following:
   * **Kind:** `Username with password`
   * **Scope:** Global (System)
   * **ID:** `db-credentials` **(Crucial: must match the Jenkinsfile)**
   * **Username:** `student` (or your actual DB user)
   * **Password:** `student` (or your actual DB password)
3. Click **Create**.

---

## 2. 📧 Email Notifications
*Required to receive build status reports.*

Navigate to: `Manage Jenkins` -> `System`.

### Step A: System Admin Email
*Find the **Jenkins Location** section.*
* **System Admin e-mail address:** Enter the **sender's email address** here (e.g., `your-bot@gmail.com`).
  *(Note: This must match the SMTP user below, otherwise sending may fail).*

### Step B: Extended E-mail Notification
*Find the **Extended E-mail Notification** section.*

* **SMTP Server:** `smtp.gmail.com` (or `poczta.o2.pl`)
* **SMTP Port:** `465` (for SSL) or `587` (for TLS)
* **Default user E-mail suffix:** Leave empty.
* Click **Advanced...**:
  * **Use SMTP Authentication:** ✅ Checked.
  * **User Name:** Your full email address.
  * **Password:** Your email password (or **App Password** if using Gmail/2FA).
  * **Use SSL:** ✅ Checked (if using port 465).
* **Test:** Click "Test configuration by sending test e-mail" at the bottom to verify connection.

---

## 3. 🐙 GitHub & Pipeline Job Setup

1. Go to **Dashboard** -> **New Item**.
2. Enter name: `todo-app-deploy` -> Select **Pipeline** -> **OK**.
3. Scroll down to the **Pipeline** section:
   * **Definition:** `Pipeline script from SCM`.
   * **SCM:** `Git`.
   * **Repository URL:** `https://github.com/YOUR-USER/YOUR-REPO.git`.
   * **Credentials:**
     * *Public Repo:* Select `- none -`.
     * *Private Repo:* Click **Add** -> **Jenkins**.
       * **Kind:** `Username with password`.
       * **Username:** Your GitHub username.
       * **Password:** Your GitHub **Personal Access Token** (Classic).
       * Select these new credentials from the dropdown.
   * **Branch Specifier:** `*/main` (or `*/master`).
   * **Script Path:** `Jenkinsfile`.
4. Click **Save**.

---

## ✅ Ready
You can now click **Build Now** to start the pipeline.