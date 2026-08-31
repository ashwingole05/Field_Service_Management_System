import { Form, Input, Select } from "antd";

export default function ServiceRequestForm({
                                             form,
                                             sites = [],
                                             sitesLoading = false,
                                           }) {

  const siteOptions =
      sites.map((site) => ({
        value: site.id,

        label: `${site.siteName} - ${site.city}`,
      }));


  return (

      <Form
          form={form}
          layout="vertical"
          requiredMark={false}
      >

        {/* SITE */}

        <Form.Item
            name="siteId"
            label="Service site"
            rules={[
              {
                required: true,
                message:
                    "Please select the site where service is required",
              },
            ]}
        >

          <Select
              placeholder="Select your service site"
              loading={sitesLoading}
              options={siteOptions}
              showSearch
              optionFilterProp="label"
              notFoundContent={
                sitesLoading
                    ? "Loading sites..."
                    : "No sites available"
              }
          />

        </Form.Item>


        {/* TITLE */}

        <Form.Item
            name="title"
            label="Issue title"
            rules={[
              {
                required: true,
                message:
                    "Please enter the issue title",
              },

              {
                min: 3,
                message:
                    "Title must contain at least 3 characters",
              },
            ]}
        >

          <Input
              placeholder="Example: AC not cooling"
          />

        </Form.Item>


        {/* DESCRIPTION */}

        <Form.Item
            name="description"
            label="Description"
            rules={[
              {
                required: true,
                message:
                    "Please describe the service issue",
              },

              {
                min: 5,
                message:
                    "Please provide a little more detail",
              },
            ]}
        >

          <Input.TextArea
              rows={5}
              placeholder=
                  "Describe the problem your team is experiencing..."
          />

        </Form.Item>

      </Form>

  );
}