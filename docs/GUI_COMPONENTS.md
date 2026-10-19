# NetBeans GUI component inventory

Each screen is a JFrame Form in `src/main/java/com/pawcare/view`. Its `.java` and `.form` files must stay together. Rename components using NetBeans Code properties. Labels, panels, scroll panes and the sidebar are also stored in the forms; the table below focuses on input controls and actions.

All workspace screens share `btnNavDashboard`, `btnNavCustomers`, `btnNavPets`, `btnNavServices`, `btnNavAppointments`, `btnNavPayments`, `btnNavReports`, `btnNavBreed`, `btnNavLogout`. Shared styling/navigation is wired after `initComponents()`; event listeners are deliberately outside guarded code.

## AppointmentFrame

| Variable | Drag from Palette | Visible text / property |
|---|---|---|
| `txtAppointmentId` | JTextField | Read-only |
| `cmbCustomer` | JComboBox |  |
| `cmbPet` | JComboBox |  |
| `cmbService` | JComboBox |  |
| `txtDate` | JTextField |  |
| `txtTime` | JTextField |  |
| `cmbStatus` | JComboBox |  |
| `btnSave` | JButton | Book appointment |
| `btnUpdate` | JButton | Update |
| `btnCancel` | JButton | Cancel appointment |
| `btnDelete` | JButton | Delete |
| `txtSearch` | JTextField |  |
| `btnSearch` | JButton | Search |
| `btnClear` | JButton | Clear form |
| `btnBack` | JButton | Back to Dashboard |
| `tblAppointments` | JTable |  |

## BreedFrame

| Variable | Drag from Palette | Visible text / property |
|---|---|---|
| `txtBreedFilter` | JTextField |  |
| `cmbBreed` | JComboBox |  |
| `btnLoadBreeds` | JButton | Load breeds |
| `btnImage` | JButton | Show breed image |
| `btnBack` | JButton | Back to Dashboard |
| `lblBreedImage` | JLabel | Load the breed list, then choose a dog breed. |

## CustomerFrame

| Variable | Drag from Palette | Visible text / property |
|---|---|---|
| `txtCustomerId` | JTextField | Read-only |
| `txtCustomerName` | JTextField |  |
| `txtPhone` | JTextField |  |
| `txtEmail` | JTextField |  |
| `txtAddress` | JTextField |  |
| `btnSave` | JButton | Add |
| `btnUpdate` | JButton | Update |
| `btnDelete` | JButton | Delete |
| `txtSearch` | JTextField |  |
| `btnSearch` | JButton | Search |
| `btnClear` | JButton | Clear form |
| `btnBack` | JButton | Back to Dashboard |
| `tblCustomers` | JTable |  |

## DashboardFrame

| Variable | Drag from Palette | Visible text / property |
|---|---|---|
| `lblTotalCustomers` | JLabel | — |
| `lblTotalPets` | JLabel | — |
| `lblTodayAppointments` | JLabel | — |
| `lblPendingAppointments` | JLabel | — |
| `lblCompletedAppointments` | JLabel | — |
| `lblTodayRevenue` | JLabel | — |
| `btnQuickBook` | JButton | Book appointment |
| `btnQuickPay` | JButton | Record payment |
| `btnRefresh` | JButton | Refresh dashboard |
| `btnPassword` | JButton | Change password |

## LoginFrame

| Variable | Drag from Palette | Visible text / property |
|---|---|---|
| `txtUsername` | JTextField |  |
| `txtPassword` | JPasswordField |  |
| `btnLogin` | JButton | Sign in |
| `btnExit` | JButton | Exit |

## PaymentFrame

| Variable | Drag from Palette | Visible text / property |
|---|---|---|
| `txtPaymentId` | JTextField | Read-only |
| `cmbAppointment` | JComboBox |  |
| `txtCustomer` | JTextField | Read-only |
| `txtService` | JTextField | Read-only |
| `txtAmount` | JTextField |  |
| `cmbMethod` | JComboBox |  |
| `txtDate` | JTextField |  |
| `btnSave` | JButton | Make payment |
| `btnUpdate` | JButton | Update |
| `btnDelete` | JButton | Delete |
| `btnReceipt` | JButton | Print receipt |
| `txtSearch` | JTextField |  |
| `btnSearch` | JButton | Search |
| `btnClear` | JButton | Clear form |
| `btnBack` | JButton | Back to Dashboard |
| `tblPayments` | JTable |  |

## PetFrame

| Variable | Drag from Palette | Visible text / property |
|---|---|---|
| `txtPetId` | JTextField | Read-only |
| `cmbCustomer` | JComboBox |  |
| `txtPetName` | JTextField |  |
| `cmbAnimalType` | JComboBox |  |
| `txtBreed` | JTextField |  |
| `cmbGender` | JComboBox |  |
| `txtAge` | JTextField |  |
| `btnSave` | JButton | Add |
| `btnUpdate` | JButton | Update |
| `btnDelete` | JButton | Delete |
| `txtSearch` | JTextField |  |
| `btnSearch` | JButton | Search |
| `btnClear` | JButton | Clear form |
| `btnBack` | JButton | Back to Dashboard |
| `tblPets` | JTable |  |

## ReportFrame

| Variable | Drag from Palette | Visible text / property |
|---|---|---|
| `txtFromDate` | JTextField |  |
| `txtToDate` | JTextField |  |
| `btnGenerateAppointments` | JButton | Appointment report |
| `btnGeneratePayments` | JButton | Payment report |
| `btnExport` | JButton | Save last report as PDF |
| `btnBack` | JButton | Back to Dashboard |

## ServiceFrame

| Variable | Drag from Palette | Visible text / property |
|---|---|---|
| `txtServiceId` | JTextField | Read-only |
| `txtServiceName` | JTextField |  |
| `txtDescription` | JTextField |  |
| `txtPrice` | JTextField |  |
| `txtDuration` | JTextField |  |
| `btnSave` | JButton | Add |
| `btnUpdate` | JButton | Update |
| `btnDelete` | JButton | Delete |
| `txtSearch` | JTextField |  |
| `btnSearch` | JButton | Search |
| `btnClear` | JButton | Clear form |
| `btnBack` | JButton | Back to Dashboard |
| `tblServices` | JTable |  |
