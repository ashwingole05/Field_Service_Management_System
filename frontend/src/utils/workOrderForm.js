import dayjs from "dayjs";

export const workOrderToForm = (row) => ({
  ...row,
  scheduledAt: row?.scheduledAt ? dayjs(row.scheduledAt) : null,
});

export const workOrderFromForm = (values) => ({
  ...values,
  scheduledAt: values.scheduledAt
    ? values.scheduledAt.format("YYYY-MM-DDTHH:mm:ss")
    : null,
});
