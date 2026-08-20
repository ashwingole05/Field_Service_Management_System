package com.FieldService.Security;

import com.FieldService.ENUM.Permissions;
import com.FieldService.ENUM.Role;

import java.util.*;

public class RoleBasedPermission {

	public static Map<Role, Set<Permissions>> getRoleWisePermissions() {

		Map<Role, Set<Permissions>> permissions = new HashMap<>();

		permissions.put(
				Role.MANAGER,
				EnumSet.of(
						Permissions.CREATE_USER,
						Permissions.UPDATE_USER,
						Permissions.DELETE_USER,
						Permissions.VIEW_USER,

						Permissions.CREATE_CUSTOMER,
						Permissions.UPDATE_CUSTOMER,
						Permissions.DELETE_CUSTOMER,
						Permissions.VIEW_CUSTOMER,

						Permissions.CREATE_SITE,
						Permissions.UPDATE_SITE,
						Permissions.DELETE_SITE,
						Permissions.VIEW_SITE,

						Permissions.CREATE_WO,
						Permissions.UPDATE_WO,
						Permissions.VIEW_WO,
						Permissions.DELETE_WO,
						Permissions.ASSIGN_WO,
						Permissions.CANCEL_WO,
						Permissions.CLOSE_WO,

						Permissions.ADD_PARTS,
						Permissions.UPDATE_PARTS,
						Permissions.VIEW_PARTS,
						Permissions.USE_PARTS,
						Permissions.DELETE_PART,

						Permissions.ADD_LOG_TIME,
						Permissions.VIEW_LOG_TIME,

						Permissions.VIEW_DASHBOARD,
						Permissions.VIEW_REPORTS,

						Permissions.SEND_NOTIFICATION,

						Permissions.REVIEW_REQUEST,
						Permissions.CONVERT_REQUEST,
						Permissions.CLOSE_REQUEST,
						Permissions.CANCEL_REQUEST
				)
		);

		permissions.put(
				Role.DISPATCHER,
				EnumSet.of(
						Permissions.CREATE_CUSTOMER,
						Permissions.UPDATE_CUSTOMER,
						Permissions.VIEW_CUSTOMER,

						Permissions.CREATE_SITE,
						Permissions.UPDATE_SITE,
						Permissions.VIEW_SITE,

						Permissions.CREATE_WO,
						Permissions.UPDATE_WO,
						Permissions.VIEW_WO,
						Permissions.ASSIGN_WO,
						Permissions.CANCEL_WO,
						Permissions.CLOSE_WO,

						Permissions.VIEW_DASHBOARD,
						Permissions.REVIEW_REQUEST,
						Permissions.CONVERT_REQUEST,
						Permissions.CLOSE_REQUEST,
						Permissions.CANCEL_REQUEST


				)
		);

		permissions.put(
				Role.TECHNICIAN,
				EnumSet.of(
						Permissions.VIEW_WO,

						Permissions.START_WORK,
						Permissions.HOLD_WORK,
						Permissions.RESUME_WORK,
						Permissions.COMPLETED_WORK,

						Permissions.USE_PARTS,
						Permissions.VIEW_PARTS,

						Permissions.ADD_LOG_TIME,
						Permissions.VIEW_LOG_TIME
				)
		);

		permissions.put(
				Role.CUSTOMER,
				EnumSet.of(
						Permissions.RAISE_REQUEST,
						Permissions.VIEW_OWN_REQUEST
				)
		);

		return permissions;
	}
}