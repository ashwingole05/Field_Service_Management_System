import {
  useCallback,
  useEffect,
  useState,
} from "react";

import {
  Button,
  Form,
  Modal,
  message,
} from "antd";

import {
  Plus,
} from "lucide-react";


import PageHeader
  from "@/components/common/PageHeader";

import ServiceRequestForm
  from "@/components/service-requests/ServiceRequestForm";

import ServiceRequestTable
  from "@/components/service-requests/ServiceRequestTable";


import useAuth
  from "@/hooks/useAuth";


import {
  createServiceRequest,
  getServiceRequests,
  getMyServiceRequests,
  reviewServiceRequest,
  convertServiceRequest,
  closeServiceRequest,
  cancelServiceRequest,
} from "@/api/serviceRequests.api";


import {
  getMySites,
} from "@/api/sites.api";


import {
  getErrorMessage,
} from "@/utils/errorHandler";


export default function ServiceRequestsPage() {

  const { user } = useAuth();


  const isCustomer =
      user?.role === "CUSTOMER";


  const [rows, setRows] =
      useState([]);


  const [sites, setSites] =
      useState([]);


  const [loading, setLoading] =
      useState(false);


  const [
    sitesLoading,
    setSitesLoading,
  ] = useState(false);


  const [open, setOpen] =
      useState(false);


  const [form] =
      Form.useForm();


  // ==========================================
  // LOAD REQUESTS
  // ==========================================

  const loadRequests =
      useCallback(
          async () => {

            setLoading(true);

            try {

              let response;


              if (isCustomer) {

                response =
                    await getMyServiceRequests();

              } else {

                response =
                    await getServiceRequests();

              }


              setRows(
                  response.data || []
              );

            } catch (error) {

              message.error(
                  getErrorMessage(
                      error,
                      "Unable to load service requests"
                  )
              );

            } finally {

              setLoading(false);

            }

          },
          [isCustomer]
      );


  // ==========================================
  // LOAD CUSTOMER SITES
  // ==========================================

  const loadSites =
      useCallback(
          async () => {

            if (!isCustomer) {
              return;
            }


            setSitesLoading(true);

            try {

              const response =
                  await getMySites();


              setSites(
                  response.data || []
              );

            } catch (error) {

              setSites([]);

              message.error(
                  getErrorMessage(
                      error,
                      "Unable to load your service sites"
                  )
              );

            } finally {

              setSitesLoading(false);

            }

          },
          [isCustomer]
      );


  // ==========================================
  // INITIAL LOAD
  // ==========================================

  useEffect(() => {

    loadRequests();


    if (isCustomer) {

      loadSites();

    }

  }, [
    isCustomer,
    loadRequests,
    loadSites,
  ]);


  // ==========================================
  // OPEN CREATE MODAL
  // ==========================================

  const openCreateModal = async () => {

    form.resetFields();


    if (
        isCustomer &&
        sites.length === 0
    ) {

      await loadSites();

    }


    setOpen(true);

  };


  // ==========================================
  // CREATE REQUEST
  // ==========================================

  const createRequest =
      async () => {

        try {

          const values =
              await form
                  .validateFields();


          const payload = {

            siteId:
            values.siteId,

            title:
                values.title.trim(),

            description:
                values.description.trim(),

          };


          await createServiceRequest(
              payload
          );


          message.success(
              "Service request raised successfully"
          );


          setOpen(false);

          form.resetFields();


          await loadRequests();

        } catch (error) {

          /*
           * Ant Design validation errors
           * do not contain response.
           */
          if (error?.response) {

            message.error(
                getErrorMessage(
                    error,
                    "Unable to raise service request"
                )
            );

          }

        }

      };


  // ==========================================
  // MANAGER / DISPATCHER ACTIONS
  // ==========================================

  const action =
      async (
          row,
          type
      ) => {

        try {

          const actionMap = {

            review:
            reviewServiceRequest,

            convert:
            convertServiceRequest,

            close:
            closeServiceRequest,

            cancel:
            cancelServiceRequest,

          };


          const actionFunction =
              actionMap[type];


          if (!actionFunction) {

            return;

          }


          await actionFunction(
              row.id
          );


          message.success(
              `Request ${type} successful`
          );


          await loadRequests();

        } catch (error) {

          message.error(
              getErrorMessage(error)
          );

        }

      };


  // ==========================================
  // UI
  // ==========================================

  return (

      <>

        <PageHeader

            eyebrow=
                "Customer care"

            title=
                "Service requests"

            description={
              isCustomer

                  ? "Raise service requests and follow your own service history."

                  : "Review customer requests and convert qualified issues into work orders."
            }

            action={

              isCustomer

                  ? (
                      <Button
                          type="primary"
                          icon={
                            <Plus size={16} />
                          }
                          onClick={
                            openCreateModal
                          }
                      >
                        New request
                      </Button>
                  )

                  : null
            }

        />


        {/* REQUEST TABLE */}

        <div className="panel table-panel">

          <ServiceRequestTable

              data={rows}

              loading={loading}

              role={user?.role}

              onAction={action}

          />

        </div>


        {/* CUSTOMER CREATE REQUEST */}

        <Modal

            title=
                "New service request"

            open={
              open
            }

            onCancel={() =>
                setOpen(false)
            }

            onOk={
              createRequest
            }

            okText=
                "Raise request"

            destroyOnHidden

        >

          <ServiceRequestForm

              form={form}

              sites={sites}

              sitesLoading={
                sitesLoading
              }

          />

        </Modal>

      </>

  );
}